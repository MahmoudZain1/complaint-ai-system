package com.ecommerce.complaints.messaging.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    public static final String COMPLAINT_EXCHANGE = "complaint.exchange";
    public static final String QUEUE_COMPLAINT_CREATED = "complaint.created.queue";
    public static final String RK_COMPLAINT_CREATED = "complaint.created";

    public static final String QUEUE_ANALYSIS_COMPLETED = "complaint.analysis.completed.queue";
    public static final String RK_ANALYSIS_COMPLETED = "complaint.analysis.completed";

    public static final String QUEUE_RESPONSE_GENERATION = "complaint.response.generation.queue";
    public static final String RK_RESPONSE_GENERATION = "complaint.response.generation";

    @Bean
    public DirectExchange complaintExchange() {
        return new DirectExchange(COMPLAINT_EXCHANGE, true, false);
    }

    @Bean
    public Queue createdQueue() {
        return QueueBuilder.durable(QUEUE_COMPLAINT_CREATED)
                .withArgument("x-dead-letter-exchange", COMPLAINT_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "complaint.created.dlq")
                .build();
    }

    @Bean
    public Queue createdDlq() {
        return QueueBuilder.durable("complaint.created.dlq")
                .build();
    }

    @Bean
    public Binding bindingDlq() {
        return BindingBuilder.bind(createdDlq())
                .to(complaintExchange())
                .with("complaint.created.dlq");
    }

    @Bean
    public Queue analysisCompletedQueue() {
        return QueueBuilder.durable(QUEUE_ANALYSIS_COMPLETED)
                .withArgument("x-dead-letter-exchange", COMPLAINT_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", "complaint.analysis.completed.dlq")
                .build();
    }

    @Bean
    public Queue analysisCompletedDlq() {
        return QueueBuilder.durable("complaint.analysis.completed.dlq").build();
    }

    @Bean
    public Binding bindingAnalysisCompleted(Queue analysisCompletedQueue, DirectExchange complaintExchange) {
        return BindingBuilder.bind(analysisCompletedQueue).to(complaintExchange)
                .with(RK_ANALYSIS_COMPLETED);
    }

    @Bean
    public Binding bindingAnalysisCompletedDlq() {
        return BindingBuilder.bind(analysisCompletedDlq()).to(complaintExchange())
                .with("complaint.analysis.completed.dlq");
    }



    @Bean
    public Binding bindingCreated(Queue createdQueue, DirectExchange complaintExchange) {
        return BindingBuilder.bind(createdQueue).to(complaintExchange)
                .with(RK_COMPLAINT_CREATED);
    }

    @Bean
    public Queue responseGenerationQueue() {
        return QueueBuilder.durable(QUEUE_RESPONSE_GENERATION)
                .build();
    }

    @Bean
    public Queue responseGenerationDlq() {
        return QueueBuilder.durable("complaint.response.generation.dlq").build();
    }

    @Bean
    public Binding bindingResponseGeneration(Queue responseGenerationQueue, DirectExchange complaintExchange) {
        return BindingBuilder.bind(responseGenerationQueue).to(complaintExchange)
                .with(RK_RESPONSE_GENERATION);
    }

    @Bean
    public Binding bindingResponseGenerationDlq() {
        return BindingBuilder.bind(responseGenerationDlq()).to(complaintExchange())
                .with("complaint.response.generation.dlq");
    }


    @Bean
    public MessageConverter jacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

}
