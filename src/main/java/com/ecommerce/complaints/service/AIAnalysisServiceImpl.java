package com.ecommerce.complaints.service;

import com.ecommerce.complaints.ai.tools.CustomerTools;
import com.ecommerce.complaints.exception.BusinessException;
import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.model.entity.ComplaintResponse;
import com.ecommerce.complaints.model.enums.ResponseStatus;
import com.ecommerce.complaints.model.generate.ComplaintAnalysisVTO;
import com.ecommerce.complaints.model.generate.ComplaintResponseVTO;
import com.ecommerce.complaints.repository.api.ComplaintRepository;
import com.ecommerce.complaints.repository.api.ComplaintResponseRepository;
import com.ecommerce.complaints.service.api.AIAnalysisService;
import com.ecommerce.complaints.service.formatter.AnalysisFormatter;
import com.ecommerce.complaints.service.mapper.ComplaintMapper;
import com.ecommerce.complaints.service.rag.PromptBuilderService;
import com.ecommerce.complaints.service.rag.RAGContextService;
import com.ecommerce.complaints.util.SearchRequestFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.ecommerce.complaints.model.error.ComplaintErrors.COMPLAINT_NOT_FOUND;
import static com.ecommerce.complaints.model.error.ComplaintErrors.RESPONSE_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIAnalysisServiceImpl implements AIAnalysisService {

    private final PromptBuilderService promptBuilderService;
    private final AnalysisFormatter analysisFormatter;
    private final RAGContextService ragContextService;
    private final ComplaintRepository complaintRepository;
    private final ComplaintResponseRepository complaintResponseRepository;
    private final ComplaintMapper complaintMapper;
    private final ChatClient chatClient;
    private final ResponseApprovalService responseApprovalService;

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
        String policyContext = ragContextService.getPolicyContextForResponse(
                complaint.getDescription()
        );
        String fullPrompt = promptBuilderService.buildAnalysisPrompt(complaint.getSubject(),
                complaint.getDescription(), policyContext,outputConverter.getFormat());

        ComplaintAnalysisVTO analysis = chatClient
                .prompt().user(fullPrompt)
                .call().entity(ComplaintAnalysisVTO.class);

        analysis.setComplaintId(complaint.getId());
        analysis.setAnalyzedAt(LocalDateTime.now());
        return analysis;
    }

    @Override
    @Transactional
    public ComplaintResponseVTO generateResponse(Long complaintId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new BusinessException(COMPLAINT_NOT_FOUND, complaintId));

        if (complaintResponseRepository.existsByComplaintId(complaintId)) {
            throw new BusinessException(RESPONSE_ALREADY_EXISTS, complaintId);
        }

        BeanOutputConverter<ComplaintResponseVTO> outputConverter =
                new BeanOutputConverter<>(ComplaintResponseVTO.class);

        List<Document> policies = ragContextService.retrievePolicyContext(complaint.getDescription());
        List<Document> similarComplaints = ragContextService.retrieveSimilarComplaints(complaint.getDescription());

        String responsePrompt = null;
        try {
            responsePrompt = promptBuilderService.buildResponsePrompt(
                    complaint.getSubject(),
                    complaint.getDescription(),
                    complaint.getId(),
                    ragContextService.buildContextString(policies),
                    ragContextService.buildContextString(similarComplaints),
                    outputConverter.getFormat()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        SearchRequest searchRequest = SearchRequestFactory.createForPolicies(complaint.getSubject() + " " + complaint.getDescription());

//        ChatClient chatClient = chatClientBuilder
//                .defaultTools(customerTools)
//                .defaultAdvisors(
//                        QuestionAnswerAdvisor.builder(vectorStore)
//                                .searchRequest(searchRequest)
//                                .build()
//                ).build();


        ComplaintResponseVTO response = chatClient
                .prompt().user(responsePrompt)
                .call().entity(ComplaintResponseVTO.class);

        response.setComplaintId(complaint.getId());
        ComplaintResponse entity = complaintMapper.toEntity(response);
        entity.setComplaint(complaint);
        entity.setStatus(ResponseStatus.PENDING_APPROVAL);
        entity.setGeneratedAt(LocalDateTime.now());
        ComplaintResponse saved = complaintResponseRepository.save(entity);
        responseApprovalService.processGeneratedResponse(saved);

        return response;
    }

    private void updateComplaintWithAnalysis(Complaint complaint, ComplaintAnalysisVTO analysis) {
        complaint.setCategory(analysis.getCategory());
        complaint.setSentiment(analysis.getSentiment());
        complaint.setPriority(analysis.getPriority());
        String analysisText = analysisFormatter.formatAnalysis(analysis);
        complaint.setAiAnalysis(analysisText);
    }

}