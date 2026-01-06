package com.ecommerce.complaints.messaging.publisher;

import com.ecommerce.complaints.messaging.api.RabbitMQEventPublisher;
import com.ecommerce.complaints.messaging.event.ComplaintEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


import static com.ecommerce.complaints.messaging.config.RabbitMQConfig.COMPLAINT_EXCHANGE;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMQEventPublisherImpl implements RabbitMQEventPublisher {

    private final RabbitTemplate rabbitTemplate;


    @Override
    public void publishEvent(ComplaintEvent event, Object payload) {
        String routingKey = event.name().toLowerCase().replace("_", ".");
        publishEvent(event, payload, routingKey);
    }

    @Override
    public void publishEvent(ComplaintEvent event, Object payload, String routingKey) {
        log.info("Publishing event: {} with routing key: {}", event, routingKey);
        rabbitTemplate.convertAndSend(COMPLAINT_EXCHANGE, routingKey, payload);
    }

    @Override
    public void sendToQueue(String queueName, Object payload) {
        log.info("Sending direct to queue: {}", queueName);
        rabbitTemplate.convertAndSend(queueName, payload);
    }
}
