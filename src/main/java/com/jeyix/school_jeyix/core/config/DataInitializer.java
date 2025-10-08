package com.jeyix.school_jeyix.core.config;

import com.jeyix.school_jeyix.core.security.enums.AuthProvider;
import com.jeyix.school_jeyix.core.security.model.Role;
import com.jeyix.school_jeyix.core.security.model.User;
import com.jeyix.school_jeyix.core.security.repository.RoleRepository;
import com.jeyix.school_jeyix.core.security.repository.UserRepository;
import com.jeyix.school_jeyix.features.customers.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

        private final RoleRepository roleRepository;
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final CustomerRepository customerRepository;

        @Override
        public void run(String... args) throws Exception {
                initRoles();
                initAdminUser();
        }

        private void initRoles() {
                String[] roles = {
                                "ROLE_CLIENT",
                                "ROLE_ADMIN",
                };

                for (String roleName : roles) {
                        roleRepository.findByName(roleName)
                                        .orElseGet(() -> roleRepository.save(new Role(null, roleName)));
                }
                System.out.println(">>> Roles inicializados");
        }

        private void initAdminUser() {
                if (userRepository.findByUsername("admin").isEmpty()) {
                        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();

                        User admin = User.builder()
                                        .username("admin")
                                        .email("DarckProyect8@gmail.com")
                                        .firstName("Joe")
                                        .lastName("Luna")
                                        .password(passwordEncoder.encode("admin123"))
                                        .roles(Collections.singleton(adminRole))
                                        .provider(AuthProvider.LOCAL)
                                        .enabled(true)
                                        .build();

                        userRepository.save(admin);
                        System.out.println(">>> Usuario admin creado: admin / admin123");
                }
        }



}