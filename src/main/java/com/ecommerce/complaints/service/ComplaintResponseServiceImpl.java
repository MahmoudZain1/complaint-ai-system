package com.ecommerce.complaints.service;

import com.ecommerce.complaints.exception.BusinessException;
import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.model.entity.ComplaintResponse;
import com.ecommerce.complaints.model.enums.ResponseStatus;
import com.ecommerce.complaints.model.generate.ComplaintResponseVTO;
import com.ecommerce.complaints.repository.api.ComplaintRepository;
import com.ecommerce.complaints.repository.api.ComplaintResponseRepository;
import com.ecommerce.complaints.service.api.ComplaintResponseService;
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
public class ComplaintResponseServiceImpl implements ComplaintResponseService {

    private final ComplaintResponseRepository complaintResponseRepository;
    private final ComplaintMapper complaintMapper;
    private final ChatClient chatClient;
    private final PromptBuilderService promptBuilderService;
    private final RAGContextService ragContextService;
    private final ResponseApprovalService responseApprovalService;
    private final ComplaintRepository complaintRepository;


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
}
