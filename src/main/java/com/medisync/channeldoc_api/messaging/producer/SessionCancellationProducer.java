package com.medisync.channeldoc_api.messaging.producer;

import com.medisync.channeldoc_api.config.RabbitMQConfig;
import com.medisync.channeldoc_api.dto.message.SessionCancellationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * Publishes {@link SessionCancellationMessage} payloads to the RabbitMQ exchange.
 *
 * <p>Follows the Single Responsibility Principle — this class is only concerned
 * with message publishing, not with the business logic of session cancellation.</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SessionCancellationProducer {

    private final RabbitTemplate rabbitTemplate;

    /**
     * Publishes a cancellation notification message to the configured exchange.
     *
     * @param message the cancellation details for one affected patient
     */
    public void sendCancellationNotification(SessionCancellationMessage message) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY,
                message
        );
        log.info("Published cancellation message to RabbitMQ for appointment [{}] to [{}]",
                message.getAppointmentNumber(), message.getPatientEmail());
    }
}
