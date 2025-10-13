package com.jeyix.school_jeyix.core.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jeyix.school_jeyix.features.notifications.dto.AnnouncementEvent;
import com.jeyix.school_jeyix.features.notifications.dto.ContactNotificationEvent;
import com.jeyix.school_jeyix.features.notifications.dto.EmailVerificationEvent;
import com.jeyix.school_jeyix.features.notifications.dto.NotifiableEvent;
import com.jeyix.school_jeyix.features.notifications.services.EmailNotificationService;
import com.jeyix.school_jeyix.features.notifications.services.FCMNotificationHandler;
import com.jeyix.school_jeyix.features.notifications.services.NotificationHandler;

@Configuration
public class NotificationHandlerConfig {

    @Bean
    public Map<String, Map<String, NotificationHandler<? extends NotifiableEvent>>> notificationHandlers(
            EmailNotificationService emailService,
            FCMNotificationHandler fcmHandler
    ) {
        Map<String, Map<String, NotificationHandler<? extends NotifiableEvent>>> handlers = new HashMap<>();

        Map<String, NotificationHandler<? extends NotifiableEvent>> emailHandlers = new HashMap<>();
        emailHandlers.put(EmailVerificationEvent.class.getSimpleName(), emailService);
        emailHandlers.put(ContactNotificationEvent.class.getSimpleName(), emailService);
        handlers.put("EMAIL", emailHandlers);
        // --- Configuración del Canal PUSH ---
        Map<String, NotificationHandler<? extends NotifiableEvent>> pushHandlers = new HashMap<>();
        pushHandlers.put(AnnouncementEvent.class.getSimpleName(), fcmHandler);
        handlers.put("PUSH", pushHandlers);

        // --- Configuración del Canal WEBSOCKET ---
        Map<String, NotificationHandler<? extends NotifiableEvent>> wsHandlers = new HashMap<>();
        handlers.put("WEBSOCKET", wsHandlers);

        return handlers;
    }

}
