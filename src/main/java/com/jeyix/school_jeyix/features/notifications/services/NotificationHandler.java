package com.jeyix.school_jeyix.features.notifications.services;


import com.jeyix.school_jeyix.features.notifications.dto.NotifiableEvent;

public interface NotificationHandler<T extends NotifiableEvent> {

    /**
     * Envía la notificación correspondiente al evento.
     * 
     * @param event evento a notificar
     */
    void send(T event);

}
