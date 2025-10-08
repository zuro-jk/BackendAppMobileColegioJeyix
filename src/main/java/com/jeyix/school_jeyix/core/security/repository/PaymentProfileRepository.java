package com.jeyix.school_jeyix.core.security.repository;

import com.jeyix.school_jeyix.core.security.model.PaymentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentProfileRepository extends JpaRepository<PaymentProfile, Long> {
}
