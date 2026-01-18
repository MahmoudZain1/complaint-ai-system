package com.ecommerce.complaints.service;

import com.ecommerce.complaints.exception.BusinessException;
import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.model.entity.ComplaintResponse;
import com.ecommerce.complaints.model.enums.ResponseStatus;
import com.ecommerce.complaints.model.error.ComplaintErrors;
import com.ecommerce.complaints.model.generate.ComplaintAnalysisVTO;
import com.ecommerce.complaints.model.generate.ComplaintResponseVTO;
import com.ecommerce.complaints.repository.api.ComplaintRepository;
import com.ecommerce.complaints.repository.api.ComplaintResponseRepository;
import com.ecommerce.complaints.service.api.ComplaintAnalysisService;
import com.ecommerce.complaints.service.formatter.AnalysisFormatter;
import com.ecommerce.complaints.service.mapper.ComplaintMapper;
import com.ecommerce.complaints.service.rag.PromptBuilderService;
import com.ecommerce.complaints.service.rag.RAGContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.document.Document;
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
public class ComplaintAnalysisServiceImpl implements ComplaintAnalysisService {

    private final PromptBuilderService promptBuilderService;
    private final AnalysisFormatter analysisFormatter;
    private final RAGContextService ragContextService;
    private final ComplaintRepository complaintRepository;
    private final ChatClient chatClient;

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
        try {
            ComplaintAnalysisVTO analysis = chatClient
                    .prompt().user(fullPrompt)
                    .call().entity(ComplaintAnalysisVTO.class);

            analysis.setComplaintId(complaint.getId());
            analysis.setAnalyzedAt(LocalDateTime.now());
            return analysis;
        }catch (Exception e){
        throw new BusinessException(ComplaintErrors.AI_ANALYSIS_FAILED, e.getMessage());
    }

    }

    private void updateComplaintWithAnalysis(Complaint complaint, ComplaintAnalysisVTO analysis) {
        complaint.setCategory(analysis.getCategory());
        complaint.setSentiment(analysis.getSentiment());
        complaint.setPriority(analysis.getPriority());
        String analysisText = analysisFormatter.formatAnalysis(analysis);
        complaint.setAiAnalysis(analysisText);
    }

}