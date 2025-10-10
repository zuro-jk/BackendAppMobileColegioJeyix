package com.jeyix.school_jeyix.core.security.service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeyix.school_jeyix.core.aws.service.FileService;
import com.jeyix.school_jeyix.core.security.dto.AuthResponse;
import com.jeyix.school_jeyix.core.security.dto.UserProfileResponse;
import com.jeyix.school_jeyix.core.security.enums.AuthProvider;
import com.jeyix.school_jeyix.core.security.jwt.JwtService;
import com.jeyix.school_jeyix.core.security.model.RefreshToken;
import com.jeyix.school_jeyix.core.security.model.Role;
import com.jeyix.school_jeyix.core.security.model.User;
import com.jeyix.school_jeyix.core.security.repository.RefreshTokenRepository;
import com.jeyix.school_jeyix.core.security.repository.RoleRepository;
import com.jeyix.school_jeyix.core.security.repository.UserRepository;
import com.jeyix.school_jeyix.features.parent.dto.parent.request.ParentRequest;
import com.jeyix.school_jeyix.features.parent.service.ParentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthUserService {

    @Value("${app.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ParentService parentService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final FileService fileService;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse processOAuthUser(String provider, String providerId,
            String email, String firstName, String lastName,
            Boolean emailVerified, String profileImageUrl) {

        String normalizedEmail = email.toLowerCase();

        Optional<User> userOpt = findByProviderId(provider, providerId);
        User user;

        if (userOpt.isPresent()) {
            user = userOpt.get();
        } else {
            Optional<User> userByEmail = userRepository.findByEmailIgnoreCase(normalizedEmail);

            if (userByEmail.isPresent()) {
                user = userByEmail.get();

                switch (provider.toLowerCase()) {
                    case "google" -> {
                        user.setProvider(AuthProvider.GOOGLE);
                        user.setGoogleId(providerId);
                    }
                    case "facebook" -> {
                        user.setProvider(AuthProvider.FACEBOOK);
                        user.setFacebookId(providerId);
                    }
                    case "github" -> {
                        user.setProvider(AuthProvider.GITHUB);
                        user.setGithubId(providerId);
                    }
                }

                userRepository.save(user);
            } else {
                user = createUserFromOAuth(provider, providerId, normalizedEmail,
                        firstName, lastName, emailVerified, profileImageUrl);
            }
        }

        String accessToken = jwtService.generateAccessToken(
                user.getUsername(),
                Map.of("roles", user.getRoles().stream().map(Role::getName).toList()));

        RefreshToken refreshTokenEntity = createRefreshToken(user);

        String finalProfileImageUrl = profileImageUrl;
        if (user.getProfileImageId() != null) {
            try {
                finalProfileImageUrl = fileService.getFileUrl(user.getProfileImageId());
            } catch (Exception e) {
                log.warn("No se pudo obtener URL de S3, usando URL del proveedor: {}", e.getMessage());
            }
        }

        UserProfileResponse userProfile = UserProfileResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .enabled(user.isEnabled())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFullName())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .phone(user.getPhone())
                .provider(user.getProvider() != null ? user.getProvider().name() : null)
                .hasPassword(user.getPassword() != null && !user.getPassword().isEmpty())
                .profileImageUrl(finalProfileImageUrl)
                .build();

        return AuthResponse.builder()
                .accessToken(accessToken)
                .sessionId(refreshTokenEntity.getId())
                .user(userProfile)
                .build();
    }

    private Optional<User> findByProviderId(String provider, String providerId) {
        return switch (provider.toLowerCase()) {
            case "google" -> userRepository.findByGoogleId(providerId);
            case "facebook" -> userRepository.findByFacebookId(providerId);
            case "github" -> userRepository.findByGithubId(providerId);
            default -> Optional.empty();
        };
    }

    private User createUserFromOAuth(String provider, String providerId, String email,
            String firstName, String lastName, Boolean emailVerified,
            String profileImageUrl) {

        User user = new User();
        user.setEmail(email);

        String baseUsername = provider.toLowerCase() + "_" + providerId;
        String username = baseUsername;
        int counter = 1;
        while (userRepository.existsByUsername(username)) {
            username = baseUsername + "_" + counter++;
        }
        user.setUsername(username);

        if (firstName == null && lastName == null) {
            user.setFirstName(email.split("@")[0]);
            user.setLastName("");
        } else if (lastName == null && firstName != null && firstName.contains(" ")) {
            String[] parts = firstName.split(" ", 2);
            user.setFirstName(parts[0]);
            user.setLastName(parts[1]);
        } else {
            user.setFirstName(firstName != null ? firstName : email.split("@")[0]);
            user.setLastName(lastName != null ? lastName : "");
        }

        user.setEnabled(true);
        user.setPassword(null);
        user.setEmailVerified(emailVerified != null && emailVerified);

        switch (provider.toLowerCase()) {
            case "google" -> {
                user.setProvider(AuthProvider.GOOGLE);
                user.setGoogleId(providerId);
            }
            case "facebook" -> {
                user.setProvider(AuthProvider.FACEBOOK);
                user.setFacebookId(providerId);
            }
            case "github" -> {
                user.setProvider(AuthProvider.GITHUB);
                user.setGithubId(providerId);
            }
            default -> {
                user.setProvider(AuthProvider.LOCAL);
                log.warn("Provider OAuth2 no soportado: {}", provider);
            }
        }

        Role clientRole = roleRepository.findByName("ROLE_CLIENT")
                .orElseThrow(() -> new RuntimeException("ROLE_CLIENT no existe"));
        user.setRoles(Set.of(clientRole));

        user.setProfileImageId(null);

        User savedUser = userRepository.save(user);

        ParentRequest parent = ParentRequest.builder()
                .userId(user.getId())
                .build();

        parentService.create(parent);

        log.info("Nuevo usuario OAuth creado: {} (provider={})", email, provider);

        return savedUser;
    }

    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(jwtService.generateRefreshToken(user.getUsername()))
                .expiryDate(Instant.now().plusMillis(refreshTokenExpiration))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }
}