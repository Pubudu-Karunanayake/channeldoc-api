package com.medisync.channeldoc_api.messaging.consumer;

import com.medisync.channeldoc_api.config.RabbitMQConfig;
import com.medisync.channeldoc_api.dto.message.SessionCancellationMessage;
import com.medisync.channeldoc_api.service.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumes {@link SessionCancellationMessage} payloads from the RabbitMQ queue
 * and triggers the actual email notification.
 *
 * <p>This consumer runs in a separate thread managed by the RabbitMQ listener container,
 * fully decoupled from the HTTP request thread that triggered the cancellation.</p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SessionCancellationConsumer {

    private final EmailNotificationService emailNotificationService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void handleCancellationNotification(SessionCancellationMessage message) {
        log.info("Consumed cancellation message from RabbitMQ for appointment [{}] on thread [{}]",
                message.getAppointmentNumber(), Thread.currentThread().getName());

        emailNotificationService.sendSessionCancellationEmail(message);
    }
}
