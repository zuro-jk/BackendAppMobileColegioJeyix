package com.jeyix.school_jeyix.features.notifications.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jeyix.school_jeyix.core.email.dto.request.EmailMessageRequest;
import com.jeyix.school_jeyix.core.email.service.EmailService;
import com.jeyix.school_jeyix.core.security.model.User;
import com.jeyix.school_jeyix.core.security.repository.UserRepository;
import com.jeyix.school_jeyix.features.notifications.dto.ContactNotificationEvent;
import com.jeyix.school_jeyix.features.notifications.dto.EmailVerificationEvent;
import com.jeyix.school_jeyix.features.notifications.dto.NotifiableEvent;
import com.jeyix.school_jeyix.features.notifications.enums.NotificationStatus;
import com.jeyix.school_jeyix.features.notifications.metrics.NotificationMetricsService;
import com.jeyix.school_jeyix.features.notifications.model.EmailNotification;
import com.jeyix.school_jeyix.features.notifications.repository.EmailNotificationRepository;
import com.jeyix.school_jeyix.features.notifications.templates.ContactEmailTemplateBuilder;
import com.jeyix.school_jeyix.features.notifications.templates.EmailTemplateBuilder;
import com.jeyix.school_jeyix.features.notifications.templates.EmailVerificationTemplateBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("EMAIL")
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationService implements NotificationHandler<NotifiableEvent> {

    @Value("${app.frontend.url}")
    private String frontendUrl;

    private final EmailService emailService;
    private final EmailNotificationRepository emailNotificationRepository;
    private final UserRepository userRepository;
    private final NotificationMetricsService notificationMetricsService;

    @Override
    public void send(NotifiableEvent event) {
        User user = resolveUser(event);
        String recipient = resolveRecipient(event, user);

        if (recipient == null) {
            handleFailed(event, user, "no_recipient");
            return;
        }

        if (isInvalidContent(event)) {
            handleFailed(event, user, "invalid_content");
            return;
        }

        String emailHtml = buildEmailHtml(event, user);

        EmailNotification email = EmailNotification.builder()
                .toAddress(recipient)
                .subject(event.getSubject())
                .body(emailHtml)
                .sentAt(LocalDateTime.now())
                .status(NotificationStatus.PENDING)
                .user(user)
                .build();

        emailNotificationRepository.save(email);
        sendWithRetries(email, recipient, emailHtml);
    }

    private User resolveUser(NotifiableEvent event) {
        if (event.getUserId() != null) {
            return userRepository.findById(event.getUserId()).orElse(null);
        }
        return null;
    }

    private String resolveRecipient(NotifiableEvent event, User user) {
        if (event.getRecipient() != null && !event.getRecipient().isBlank()) {
            return event.getRecipient();
        }
        return user != null ? user.getEmail() : null;
    }

    private boolean isInvalidContent(NotifiableEvent event) {
        return event.getSubject() == null || event.getSubject().isBlank() ||
                event.getMessage() == null || event.getMessage().isBlank();
    }

    private void handleFailed(NotifiableEvent event, User user, String reason) {
        log.warn("⚠️ Notificación fallida: {} para userId={}", reason, event.getUserId());
        notificationMetricsService.incrementFailed("EMAIL", reason);

        EmailNotification failedEmail = EmailNotification.builder()
                .toAddress(event.getRecipient())
                .subject(event.getSubject())
                .body(event.getMessage())
                .sentAt(LocalDateTime.now())
                .status(NotificationStatus.FAILED)
                .user(user)
                .build();

        emailNotificationRepository.save(failedEmail);
    }

    private String buildEmailHtml(NotifiableEvent event, User user) {
        String recipientName = user != null ? user.getFullName() : "Cliente";

        return switch (event.getEventType()) {
            case "EmailVerificationEvent" -> EmailVerificationTemplateBuilder
                    .buildVerificationEmail((EmailVerificationEvent) event, recipientName, event.getActionUrl());
            case "ContactNotificationEvent" -> ContactEmailTemplateBuilder.buildContactEmail(
                    recipientName,
                    event.getRecipient(),
                    ((ContactNotificationEvent) event).getPhone(),
                    event.getSubject(),
                    event.getMessage(),
                    event.getActionUrl());
            default -> EmailTemplateBuilder.buildPromotionEmail(
                    event.getSubject(),
                    event.getMessage(),
                    event.getActionUrl() != null ? event.getActionUrl() : "#");
        };
    }

    private void sendWithRetries(EmailNotification email, String recipient, String emailHtml) {
        int attempts = 0;
        boolean sent = false;

        while (attempts < 3 && !sent) {
            try {
                emailService.sendEmail(EmailMessageRequest.builder()
                        .toAddress(recipient)
                        .subject(email.getSubject())
                        .body(emailHtml)
                        .build());
                sent = true;
                email.setStatus(NotificationStatus.SENT);
                notificationMetricsService.incrementSent("EMAIL");
                log.info("📧 Notificación enviada a {} en intento #{}", recipient, attempts + 1);
            } catch (Exception ex) {
                attempts++;
                log.warn("⚠️ Intento #{} fallido para {}: {}", attempts, recipient, ex.getMessage());
                if (attempts == 3) {
                    email.setStatus(NotificationStatus.FAILED);
                    notificationMetricsService.incrementFailed("EMAIL", "smtp_error");
                    log.error("❌ No se pudo enviar el correo a {} después de 3 intentos", recipient, ex);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
        }

        emailNotificationRepository.save(email);
    }
}
