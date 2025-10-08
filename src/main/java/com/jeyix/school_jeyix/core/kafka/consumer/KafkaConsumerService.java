package com.jeyix.school_jeyix.core.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeyix.school_jeyix.core.kafka.message.KafkaMessage;
import com.jeyix.school_jeyix.features.notifications.dto.NotifiableEvent;
import com.jeyix.school_jeyix.features.notifications.facade.NotificationFacade;
import com.jeyix.school_jeyix.features.notifications.registry.EventTypeRegistry;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final ObjectMapper objectMapper;
    private final NotificationFacade notificationFacade;
    private final EventTypeRegistry eventTypeRegistry;

    @KafkaListener(topics = "notifications", groupId = "san-isidro-group")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            KafkaMessage message = objectMapper.readValue(record.value(), KafkaMessage.class);
            String payloadJson = message.getPayload();

            String eventType = objectMapper.readTree(payloadJson).get("eventType").asText();
            Class<? extends NotifiableEvent> eventClass = eventTypeRegistry.getEventClass(eventType);

            if (eventClass == null) {
                logger.warn("⚠️ Tipo de evento desconocido: {}", payloadJson);
                return;
            }

            NotifiableEvent event = objectMapper.readValue(payloadJson, eventClass);
            notificationFacade.processNotification(event);

            logger.info("✅ Evento procesado correctamente: key={}", message.getKey());

        } catch (Exception e) {
            logger.error("❌ Error al procesar el mensaje de Kafka", e);
        }
    }
}