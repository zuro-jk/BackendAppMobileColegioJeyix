package com.jeyix.school_jeyix.core.audit.repository;

import com.jeyix.school_jeyix.core.audit.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
