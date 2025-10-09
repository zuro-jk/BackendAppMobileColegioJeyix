package com.jeyix.school_jeyix.features.notifications.registry;

import com.jeyix.school_jeyix.features.notifications.dto.NotifiableEvent;
import org.reflections.Reflections;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class EventTypeRegistry {

    private final Map<String, Class<? extends NotifiableEvent>> eventTypeMap = new HashMap<>();

    @PostConstruct
    public void init() {
        Reflections reflections = new Reflections("com.jeyix.school_jeyix.features.notifications.dto");

        Set<Class<? extends NotifiableEvent>> eventClasses = reflections.getSubTypesOf(NotifiableEvent.class);
        for (Class<? extends NotifiableEvent> clazz : eventClasses) {
            eventTypeMap.put(clazz.getSimpleName(), clazz);
        }

        System.out.println("✅ EventTypeRegistry cargado con: " + eventTypeMap.keySet());
    }

    public Class<? extends NotifiableEvent> getEventClass(String eventType) {
        return eventTypeMap.get(eventType);
    }

}
