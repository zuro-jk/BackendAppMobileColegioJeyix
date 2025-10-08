package com.jeyix.school_jeyix.core.security.repository;


import com.jeyix.school_jeyix.core.security.model.RefreshToken;
import com.jeyix.school_jeyix.core.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByExpiryDateBefore(Instant now);

    void deleteByToken(String token);

    void deleteAllByUser(User user);

    List<RefreshToken> findAllByUser(User user);
}
