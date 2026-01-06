package com.ecommerce.complaints.messaging.api;

import com.ecommerce.complaints.messaging.event.ComplaintEvent;

public interface RabbitMQEventPublisher {

    void publishEvent(ComplaintEvent event, Object payload);
    void publishEvent(ComplaintEvent event, Object payload, String routingKey);
    void sendToQueue(String queueName, Object payload);
}
