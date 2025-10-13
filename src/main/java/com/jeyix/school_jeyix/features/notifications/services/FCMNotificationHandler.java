package com.jeyix.school_jeyix.features.notifications.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jeyix.school_jeyix.core.security.model.User;
import com.jeyix.school_jeyix.core.security.repository.UserRepository;
import com.jeyix.school_jeyix.features.notifications.dto.AnnouncementEvent;
import com.jeyix.school_jeyix.features.notifications.dto.NotifiableEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("PUSH")
@RequiredArgsConstructor
@Slf4j
public class FCMNotificationHandler implements NotificationHandler<NotifiableEvent> {

    private final FCMService fcmService;
    private final UserRepository userRepository;

    @Override
    public void send(NotifiableEvent event) {
        if (event instanceof AnnouncementEvent announcementEvent) {
            handleAnnouncement(announcementEvent);
        } else {
            handleSingleUser(event);
        }
    }

    private void handleAnnouncement(AnnouncementEvent event) {
        log.info("📢 Procesando anuncio para los roles: {}", event.getTargetRoles());
        List<User> targetUsers;

        if (event.getTargetRoles().contains("ALL")) {
            targetUsers = userRepository.findAll();
        } else {
            targetUsers = userRepository.findAllByRoles_NameIn(event.getTargetRoles());
        }

        log.info("📢 Encontrados {} usuarios destinatarios.", targetUsers.size());
        for (User user : targetUsers) {
            if (user.getDeviceToken() != null && !user.getDeviceToken().isBlank()) {
                fcmService.sendNotification(
                        user.getDeviceToken(),
                        event.getSubject(),
                        event.getMessage());
            }
        }
    }

    private void handleSingleUser(NotifiableEvent event) {
        if (event.getUserId() == null) {
            log.warn("⚠️ Evento de notificación Push individual sin userId. No se puede enviar.");
            return;
        }

        User user = userRepository.findById(event.getUserId()).orElse(null);

        if (user == null || user.getDeviceToken() == null || user.getDeviceToken().isBlank()) {
            log.warn(
                    "⚠️ Usuario con ID {} no encontrado o sin token de dispositivo. No se puede enviar notificación Push.",
                    event.getUserId());
            return;
        }

        fcmService.sendNotification(
                user.getDeviceToken(),
                event.getSubject(),
                event.getMessage());
    }

}
