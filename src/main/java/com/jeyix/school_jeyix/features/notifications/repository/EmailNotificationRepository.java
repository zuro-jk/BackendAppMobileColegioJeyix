package com.jeyix.school_jeyix.features.notifications.repository;

import com.jeyix.school_jeyix.features.notifications.model.EmailNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailNotificationRepository extends JpaRepository<EmailNotification, Long> {
}
