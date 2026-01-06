package com.ecommerce.complaints.service;

import com.ecommerce.complaints.ai.tools.CustomerTools;
import com.ecommerce.complaints.config.PolicyPromptConfig;
import com.ecommerce.complaints.config.aspect.annotation.LogClass;
import com.ecommerce.complaints.exception.BusinessException;
import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.model.entity.ComplaintResponse;
import com.ecommerce.complaints.model.vto.ComplaintAnalysisVTO;
import com.ecommerce.complaints.model.vto.ComplaintResponseVTO;
import com.ecommerce.complaints.repoistory.api.ComplaintRepository;
import com.ecommerce.complaints.repoistory.api.ComplaintResponseRepository;
import com.ecommerce.complaints.service.api.AIAnalysisService;
import com.ecommerce.complaints.service.config.PromptAdapterConfig;
import com.ecommerce.complaints.service.mapper.ComplaintMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static com.ecommerce.complaints.model.enums.ComplaintErrors.COMPLAINT_NOT_FOUND;
import static com.ecommerce.complaints.model.enums.ComplaintErrors.RESPONSE_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
@LogClass
@Slf4j
public class AIAnalysisServiceImpl implements AIAnalysisService {

    private final ChatClient.Builder chatClientBuilder;
    private final PromptAdapterConfig promptAdapterConfig;
    private final ComplaintRepository complaintRepository;
    private final ComplaintResponseRepository complaintResponseRepository;
    private final VectorStore vectorStore;
    private final ComplaintMapper complaintMapper;
    private final CustomerTools customerTools;
    private final PolicyPromptConfig policyPromptConfig;

    public void processAiAnalysis(Long complaintId, String content) throws IOException {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new BusinessException(COMPLAINT_NOT_FOUND, complaintId));
        ComplaintAnalysisVTO analysis = analyzeComplaint(complaint);
        updateComplaintWithAnalysis(complaint, analysis);
        complaintRepository.update(complaint);
    }

    @Override
    public ComplaintAnalysisVTO analyzeComplaint(Complaint complaint) throws IOException {
        BeanOutputConverter<ComplaintAnalysisVTO> outputConverter =
                new BeanOutputConverter<>(ComplaintAnalysisVTO.class);

        String fullPrompt = buildAnalysisPrompt(
                complaint.getSubject(),
                complaint.getDescription(),
                outputConverter.getFormat()
        );

        ComplaintAnalysisVTO analysis = chatClientBuilder.build()
                .prompt()
                .user(fullPrompt)
                .call()
                .entity(ComplaintAnalysisVTO.class);

        analysis.setComplaintId(complaint.getId());
        analysis.setAnalyzedAt(LocalDateTime.now());
        return analysis;
    }

    @Override
    @Transactional
    public ComplaintResponseVTO generateResponse(Long complaintId) throws IOException {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new BusinessException(COMPLAINT_NOT_FOUND, complaintId));

        if (complaintResponseRepository.existsByComplaintId(complaintId)) {
            throw new BusinessException(RESPONSE_ALREADY_EXISTS, complaintId);
        }

        BeanOutputConverter<ComplaintResponseVTO> outputConverter =
                new BeanOutputConverter<>(ComplaintResponseVTO.class);

        String responsePrompt = buildResponsePrompt(
                complaint.getSubject(),
                complaint.getDescription(),
                complaint.getId(),
                outputConverter.getFormat()
        );

        ChatClient chatClient = chatClientBuilder
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore)
                        .searchRequest(SearchRequest.builder().topK(7)
                                .similarityThreshold(0.5)
                                .build()).build()).build();


        ComplaintResponseVTO response = chatClient.prompt().user(responsePrompt)
                .call().entity(ComplaintResponseVTO.class);

        response.setComplaintId(complaint.getId());
        ComplaintResponse entity = complaintMapper.toEntity(response);
        entity.setComplaint(complaint);
        complaintResponseRepository.save(entity);

        return response;
    }

    private void updateComplaintWithAnalysis(Complaint complaint, ComplaintAnalysisVTO analysis) {
        complaint.setCategory(analysis.getCategory());
        complaint.setSentiment(analysis.getSentiment());
        complaint.setPriority(analysis.getPriority());
        StringBuilder analysisText = new StringBuilder();

        analysisText.append("Category: ").append(analysis.getCategory());
        if (analysis.getCategoryConfidence() != null) {
            analysisText.append(" (confidence: ")
                    .append(String.format("%.1f%%", analysis.getCategoryConfidence() * 100))
                    .append(")");
        }
        analysisText.append("\n\n");

        analysisText.append("Sentiment: ").append(analysis.getSentiment());
        if (analysis.getSentimentScore() != null) {
            analysisText.append(" (score: ")
                    .append(String.format("%.2f", analysis.getSentimentScore()))
                    .append(")");
        }
        analysisText.append("\n\n");
        analysisText.append("Priority: ").append(analysis.getPriority());
        if (analysis.getUrgencyScore() != null) {
            analysisText.append(" (urgency: ")
                    .append(String.format("%.1f%%", analysis.getUrgencyScore() * 100))
                    .append(")");
        }
        analysisText.append("\n");

        if (analysis.getUrgencyReason() != null && !analysis.getUrgencyReason().isEmpty()) {
            analysisText.append("   Reason: ").append(analysis.getUrgencyReason());
        }
        analysisText.append("\n\n");

        if (analysis.getKeywords() != null && !analysis.getKeywords().isEmpty()) {
            analysisText.append("Keywords:\n");
            analysis.getKeywords().forEach(keyword ->
                    analysisText.append("   • ").append(keyword).append("\n")
            );
            analysisText.append("\n");
        }
        if (analysis.getSummary() != null && !analysis.getSummary().isEmpty()) {
            analysisText.append("Summary:\n");
            analysisText.append(analysis.getSummary()).append("\n\n");
        }

        analysisText.append("Analyzed at: ").append(analysis.getAnalyzedAt());

        complaint.setAiAnalysis(analysisText.toString());
    }

    private String buildAnalysisPrompt(String subject, String description, String format) throws IOException {
        String analysisTemplate = readResource(promptAdapterConfig.getAnalysisTemplate());
        String classificationPrompt = readResource(promptAdapterConfig.getClassificationPrompt());
        String sentimentPrompt = readResource(promptAdapterConfig.getSentimentPrompt());
        String urgencyPrompt = readResource(promptAdapterConfig.getUrgencyPrompt());
        String keywordPrompt = readResource(promptAdapterConfig.getKeywordsPrompt());
        return analysisTemplate
                .replace("{subject}", subject != null ? subject : "")
                .replace("{content}", description != null ? description : "")
                .replace("{classificationPrompt}", classificationPrompt)
                .replace("{sentimentPrompt}", sentimentPrompt)
                .replace("{urgencyPrompt}", urgencyPrompt)
                .replace("{keywordPrompt}", keywordPrompt)
                .replace("{format}", format);
    }

    private String buildResponsePrompt(String subject, String description,
                                       Long complaintId, String format) throws IOException {
        String responseTemplate = readResource(promptAdapterConfig.getResponsePrompt());

        String safeSubject = policyPromptConfig.escapeHtml(subject);
        String safeDescription = policyPromptConfig.escapeHtml(description);

        return responseTemplate
                .replace("{complaintId}", complaintId.toString())
                .replace("{complaintSubject}", safeSubject)
                .replace("{complaintDescription}", safeDescription)
                .replace("{format}", format);
    }

    private String readResource(Resource resource) throws IOException {
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }
}