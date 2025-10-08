package com.jeyix.school_jeyix.features.notifications.services;


import com.jeyix.school_jeyix.features.notifications.dto.NotifiableEvent;

public interface NotificationChannel {
    void send(NotifiableEvent event);
}