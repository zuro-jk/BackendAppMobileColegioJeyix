package com.jeyix.school_jeyix.features.notifications.facade;

import com.jeyix.school_jeyix.features.notifications.dto.NotifiableEvent;
import com.jeyix.school_jeyix.features.notifications.services.NotificationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationFacade {

    /**
     * Mapa de handlers por canal y tipo de evento
     * Map<Canal, Map<TipoEvento, Handler>>
     */
    private final Map<String, Map<String, NotificationHandler<? extends NotifiableEvent>>> handlers;

    /**
     * Procesa un evento y lo enruta al handler correspondiente
     */
    public void processNotification(NotifiableEvent event) {
        String channelKey = event.getChannelKey().toUpperCase();
        Map<String, NotificationHandler<? extends NotifiableEvent>> channelHandlers = handlers.get(channelKey);

        if (channelHandlers == null) {
            throw new IllegalArgumentException("No se encontró handlers para el canal: " + channelKey);
        }

        String eventType = event.getEventType();
        NotificationHandler handler = channelHandlers.get(eventType);

        if (handler == null) {
            throw new IllegalArgumentException(
                    "No se encontró un handler para el evento " + eventType + " en el canal " + channelKey);
        }

        handler.send(event);
    }
}
