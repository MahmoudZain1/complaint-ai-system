package com.ecommerce.complaints.messaging.consumer;

import com.ecommerce.complaints.messaging.api.RabbitMQEventPublisher;
import com.ecommerce.complaints.model.generate.ComplaintResponseVTO;
import com.ecommerce.complaints.service.ComplaintResponseServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.ecommerce.complaints.messaging.config.RabbitMQConfig.QUEUE_ANALYSIS_COMPLETED;
import static com.ecommerce.complaints.messaging.event.ComplaintEvent.COMPLAINT_RESPONSE_GENERATED;

@Component
@RequiredArgsConstructor
@Slf4j
public class handleResponseGenerationRequest {

    private final ComplaintResponseServiceImpl complaintResponseService;
    private final RabbitMQEventPublisher eventPublisher;

    @RabbitListener(queues = QUEUE_ANALYSIS_COMPLETED)
    public void handleResponseGenerationRequest(@Payload Map<String, Object> payload){
            Long complaintId = Long.valueOf(payload.get("complaintId").toString());
            ComplaintResponseVTO response = complaintResponseService.generateResponse(complaintId);
            eventPublisher.publishEvent(COMPLAINT_RESPONSE_GENERATED, Map.of("complaintId", response.getComplaintId()));

    }
}
