package com.ecommerce.complaints.service;

import com.ecommerce.complaints.config.aspect.annotation.LogClass;
import com.ecommerce.complaints.exception.BusinessException;

import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.model.entity.ComplaintResponse;
import com.ecommerce.complaints.model.entity.User;
import com.ecommerce.complaints.model.enums.ComplaintStatus;
import com.ecommerce.complaints.model.enums.Priority;
import com.ecommerce.complaints.model.enums.ResponseStatus;

import com.ecommerce.complaints.model.error.ResponseErrors;
import com.ecommerce.complaints.model.generate.ResponseApprovalDTO;
import com.ecommerce.complaints.model.generate.ResponseReviewVTO;
import com.ecommerce.complaints.repository.api.ComplaintRepository;
import com.ecommerce.complaints.repository.api.ComplaintResponseRepository;
import com.ecommerce.complaints.repository.api.UserRepository;
import com.ecommerce.complaints.service.mapper.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.ecommerce.complaints.model.error.ComplaintErrors.COMPLAINT_NOT_FOUND;
import static com.ecommerce.complaints.model.error.UserErrors.INVALID_CREDENTIALS;

@Service
@RequiredArgsConstructor
@LogClass
public class ResponseApprovalService {

    private final ComplaintResponseRepository responseRepository;
    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final EmailNotificationService emailService;
    private final ResponseMapper responseMapper;

    private static final double AUTO_APPROVE_CONFIDENCE = 0.85;


    @Transactional
    public void processGeneratedResponse(ComplaintResponse response) {
        Complaint complaint = response.getComplaint();
        boolean shouldAutoApprove = shouldAutoApprove(response);

        if (shouldAutoApprove) {
            response.setStatus(ResponseStatus.APPROVED);
            response.setAutoApproved(true);
            response.getComplaint().setStatus(ComplaintStatus.RESOLVED);

            complaintRepository.update(complaint);
            responseRepository.save(response);
            sendToCustomer(response);

        } else {
            response.setStatus(ResponseStatus.PENDING_APPROVAL);
            response.getComplaint().setStatus(ComplaintStatus.PENDING_REVIEW);
            complaintRepository.update(complaint);
            responseRepository.save(response);
            emailService.notifyEmployeesOfPendingApproval(
                    response.getComplaint().getSubject(),
                    response.getComplaint().getId()
            );

            emailService.notifyCustomerUnderReview(
                    response.getComplaint().getCustomerEmail(),
                    response.getComplaint().getSubject()
            );
        }
    }


    private boolean shouldAutoApprove(ComplaintResponse response) {
        Priority priority = response.getComplaint().getPriority();
        Double confidence = response.getConfidenceScore();

        if (confidence == null) {
            return false;
        }
        return (priority == Priority.LOW || priority == Priority.MEDIUM)
                && confidence >= AUTO_APPROVE_CONFIDENCE;
    }


    public List<ResponseReviewVTO> getPendingApprovals() {
        List<ComplaintResponse> pending = responseRepository.findPendingApprovals();
        return pending.stream()
                .map(responseMapper::toReviewVTO)
                .collect(Collectors.toList());
    }


    public long getPendingApprovalsCount() {
        return responseRepository.countByStatus(ResponseStatus.PENDING_APPROVAL);
    }


    @Transactional
    public ResponseReviewVTO reviewResponse(ResponseApprovalDTO approval) {
        ComplaintResponse response = responseRepository.findById(approval.getResponseId())
                .orElseThrow(() -> new BusinessException(COMPLAINT_NOT_FOUND, approval.getResponseId()));

        if (response.getStatus() != ResponseStatus.PENDING_APPROVAL) {
            throw new BusinessException(ResponseErrors.INVALID_RESPONSE_STATUS);

        }

        User reviewer = getCurrentUser();

        switch (approval.getAction().toString().toUpperCase()) {
            case "APPROVE":
                handleApproval(response, reviewer);
                break;
            case "EDIT":
                handleEdit(response, reviewer, approval.getEditedResponse());
                break;
            case "REJECT":
                handleRejection(response, reviewer, approval.getRejectionReason());
                break;
            default:
                throw new BusinessException(ResponseErrors.INVALID_ACTION);
        }

        ComplaintResponse saved = responseRepository.save(response);
        return responseMapper.toReviewVTO(saved);
    }

    private void handleApproval(ComplaintResponse response, User reviewer) {
        response.setStatus(ResponseStatus.APPROVED);
        response.setReviewedBy(reviewer);
        response.setReviewedAt(LocalDateTime.now());
        response.getComplaint().setStatus(ComplaintStatus.RESOLVED);

        sendToCustomer(response);
    }

    private void handleEdit(ComplaintResponse response, User reviewer, String editedResponse) {
        if (editedResponse == null || editedResponse.trim().isEmpty()) {
            throw new BusinessException(ResponseErrors.EMPTY_EDITED_RESPONSE);
        }

        response.setStatus(ResponseStatus.EDITED);
        response.setEditedResponse(editedResponse);
        response.setReviewedBy(reviewer);
        response.setReviewedAt(LocalDateTime.now());
        response.getComplaint().setStatus(ComplaintStatus.RESOLVED);

        sendToCustomer(response);
    }

    private void handleRejection(ComplaintResponse response, User reviewer, String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new BusinessException(ResponseErrors.REJECTION_REASON_REQUIRED);
        }
        response.setStatus(ResponseStatus.REJECTED);
        response.setRejectionReason(reason);
        response.setReviewedBy(reviewer);
        response.setReviewedAt(LocalDateTime.now());
        response.getComplaint().setStatus(ComplaintStatus.REJECTED);
    }


    private void sendToCustomer(ComplaintResponse response) {
        String finalResponse = response.getEditedResponse() != null
                ? response.getEditedResponse()
                : response.getGeneratedResponse();

        emailService.sendComplaintResponse(
                response.getComplaint().getCustomerEmail(),
                response.getComplaint().getSubject(),
                finalResponse
        );

        response.setStatus(ResponseStatus.SENT);
        response.setSentAt(LocalDateTime.now());
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new BusinessException(INVALID_CREDENTIALS);
        }
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new BusinessException(INVALID_CREDENTIALS));
    }
}