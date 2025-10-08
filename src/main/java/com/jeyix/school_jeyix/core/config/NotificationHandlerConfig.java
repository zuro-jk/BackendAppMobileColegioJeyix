package com.jeyix.school_jeyix.core.config;

import com.jeyix.school_jeyix.features.notifications.dto.EmailVerificationEvent;
import com.jeyix.school_jeyix.features.notifications.dto.NotifiableEvent;
import com.jeyix.school_jeyix.features.notifications.services.EmailNotificationService;
import com.jeyix.school_jeyix.features.notifications.services.NotificationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class NotificationHandlerConfig {

    @Bean
    public Map<String, Map<String, NotificationHandler<? extends NotifiableEvent>>> notificationHandlers(
            EmailNotificationService emailService) {
        Map<String, Map<String, NotificationHandler<? extends NotifiableEvent>>> handlers = new HashMap<>();

        Map<String, NotificationHandler<? extends NotifiableEvent>> emailHandlers = new HashMap<>();
        emailHandlers.put(EmailVerificationEvent.class.getSimpleName(), emailService);

        handlers.put("EMAIL", emailHandlers);

        Map<String, NotificationHandler<? extends NotifiableEvent>> wsHandlers = new HashMap<>();
        handlers.put("WEBSOCKET", wsHandlers);

        return handlers;
    }

}
