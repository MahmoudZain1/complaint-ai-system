package com.ecommerce.complaints.messaging.consumer;

import com.ecommerce.complaints.messaging.api.RabbitMQEventPublisher;
import com.ecommerce.complaints.messaging.event.ComplaintAiAnalysiEvent;

import com.ecommerce.complaints.service.api.AIAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


import java.util.Map;

import static com.ecommerce.complaints.messaging.config.RabbitMQConfig.QUEUE_COMPLAINT_CREATED;
import static com.ecommerce.complaints.messaging.event.ComplaintEvent.COMPLAINT_ANALYSIS_COMPLETED;

@Component
@RequiredArgsConstructor
@Slf4j
public class AiAnalysisConsumer {

    private final AIAnalysisService aiAnalysisService;
    private final RabbitMQEventPublisher eventPublisher;

    @RabbitListener(queues = QUEUE_COMPLAINT_CREATED)
    public void handleAiAnalysisRequest(@Payload ComplaintAiAnalysiEvent event) {
        try {
            aiAnalysisService.processAiAnalysis(event.getComplaintId(), event.getContent());
            eventPublisher.publishEvent(COMPLAINT_ANALYSIS_COMPLETED, Map.of("complaintId", event.getComplaintId()));
        } catch (Exception ex) {
            log.error("Analysis failed for complaint {}", event.getComplaintId(), ex);
        }
    }


}
