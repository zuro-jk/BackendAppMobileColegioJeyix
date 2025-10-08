package com.jeyix.school_jeyix.core.email.repository;

import com.jeyix.school_jeyix.core.email.model.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailLogRepository extends JpaRepository<EmailLog, Long> {

    Optional<EmailLog> findFirstByToAddressAndSubjectOrderBySentAtDesc(String toAddress, String subject);
}