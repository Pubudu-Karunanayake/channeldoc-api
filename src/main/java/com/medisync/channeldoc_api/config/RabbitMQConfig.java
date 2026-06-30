package com.medisync.channeldoc_api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ infrastructure configuration.
 *
 * <p>Uses a Topic Exchange with a durable queue for session cancellation
 * notifications. The {@link Jackson2JsonMessageConverter} is configured with
 * a custom {@link ObjectMapper} that registers {@link JavaTimeModule} to
 * correctly serialize/deserialize Java 8 date/time types (e.g. {@code LocalDate}).</p>
 *
 * <p><b>Pitfall #1 Fix:</b> Without the JavaTimeModule, the default ObjectMapper
 * cannot handle {@code LocalDate} fields, causing serialization failures
 * when publishing messages to RabbitMQ.</p>
 */
@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "session.cancellation.notification.queue";
    public static final String EXCHANGE = "session.cancellation.exchange";
    public static final String ROUTING_KEY = "session.cancellation.routing-key";

    @Bean
    public Queue sessionCancellationQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean
    public TopicExchange sessionCancellationExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding sessionCancellationBinding(Queue sessionCancellationQueue, TopicExchange sessionCancellationExchange) {
        return BindingBuilder
                .bind(sessionCancellationQueue)
                .to(sessionCancellationExchange)
                .with(ROUTING_KEY);
    }

    /**
     * Custom Jackson {@link MessageConverter} with {@link JavaTimeModule} registered.
     * Ensures {@code LocalDate} and other Java 8 date/time types are serialized
     * as ISO-8601 strings (e.g. "2026-06-30") instead of numeric arrays.
     */
    @Bean
    public MessageConverter jacksonMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jacksonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jacksonMessageConverter);
        return rabbitTemplate;
    }
}
