package com.ecommerce.complaints.service;

import com.ecommerce.complaints.common.TestDataFactory;
import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.model.entity.ComplaintResponse;
import com.ecommerce.complaints.model.entity.User;
import com.ecommerce.complaints.model.enums.Priority;
import com.ecommerce.complaints.model.enums.ResponseStatus;
import com.ecommerce.complaints.model.generate.ResponseApprovalDTO;
import com.ecommerce.complaints.repository.api.ComplaintRepository;
import com.ecommerce.complaints.repository.api.ComplaintResponseRepository;
import com.ecommerce.complaints.repository.api.UserRepository;
import com.ecommerce.complaints.service.mapper.ResponseMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Response Approval Service Unit Tests")
public class ResponseApprovalServiceTes {

    @Mock private ComplaintResponseRepository responseRepository;
    @Mock private ComplaintRepository complaintRepository;
    @Mock private EmailNotificationService emailService;
    @Mock private UserRepository userRepository;
    @Mock private ResponseMapper responseMapper;

    ArgumentCaptor<ComplaintResponse> captor = ArgumentCaptor.forClass(ComplaintResponse.class);

    @InjectMocks
    private ResponseApprovalService responseApprovalService;

    @Test
    void processGeneratedResponse_shouldAutoApprove_whenConfidenceIsHighAndPriorityIsLow() {
        Complaint lowPriorityComplaint = TestDataFactory.createSampleComplaint();
        lowPriorityComplaint.setPriority(Priority.LOW);

        ComplaintResponse response = new ComplaintResponse();
        response.setComplaint(lowPriorityComplaint);
        response.setConfidenceScore(0.90);
        response.setGeneratedResponse("This is a sample generated response.");
        responseApprovalService.processGeneratedResponse(response);
        verify(responseRepository).save(captor.capture());
        ComplaintResponse savedResponse = captor.getValue();

        assertThat(savedResponse.getStatus()).isEqualTo(ResponseStatus.SENT);
        assertThat(savedResponse.getAutoApproved()).isTrue();

        verify(emailService).sendComplaintResponse(eq("zain@gmail.com"), eq("Original Subject"), eq("This is a sample generated response."));
        verify(emailService, never()).notifyEmployeesOfPendingApproval(any(), any());
    }


    @Test
    void processGeneratedResponse_shouldSetToPending_whenConfidenceIsLow() {
        Complaint complaint = TestDataFactory.createSampleComplaint();

        ComplaintResponse response = new ComplaintResponse();
        response.setComplaint(complaint);
        response.setConfidenceScore(0.50);
        responseApprovalService.processGeneratedResponse(response);
        verify(responseRepository).save(captor.capture());
        ComplaintResponse savedResponse = captor.getValue();
        assertThat(savedResponse.getStatus()).isEqualTo(ResponseStatus.PENDING_APPROVAL);
        assertThat(savedResponse.getAutoApproved()).isFalse();
        verify(emailService).notifyEmployeesOfPendingApproval(any(), any());
        verify(emailService).notifyCustomerUnderReview(any(), any());
        verify(emailService, never()).sendComplaintResponse(anyString(), anyString(), anyString());
    }


    @Test
    void reviewResponse_shouldHandleApproval_correctly() {
           User manager = TestDataFactory.createSampleManager();
           ComplaintResponse pendingResponse = TestDataFactory.createSamplePendingResponse();

           ResponseApprovalDTO approvalDTO = ResponseApprovalDTO.builder()
                .responseId(pendingResponse.getId())
                .action(ResponseApprovalDTO.ActionEnum.APPROVE)
                .build();

            when(responseRepository.findById(pendingResponse.getId())).thenReturn(Optional.of(pendingResponse));
            MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class);
            Authentication auth = mock(Authentication.class);
            SecurityContext securityContext = mock(SecurityContext.class);
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(auth);
            when(auth.isAuthenticated()).thenReturn(true);
            when(auth.getName()).thenReturn(manager.getEmail());
            when(userRepository.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));

            responseApprovalService.reviewResponse(approvalDTO);


            verify(responseRepository).save(captor.capture());
            ComplaintResponse savedResponse = captor.getValue();
            assertThat(savedResponse.getStatus()).isEqualTo(ResponseStatus.SENT);
            assertThat(savedResponse.getReviewedBy()).isEqualTo(manager);
            assertThat(savedResponse.getReviewedAt()).isNotNull();

            verify(emailService).sendComplaintResponse(anyString(), anyString(), anyString());


    }

    @Test
    @SneakyThrows
    void reviewResponse_shouldHandleRejection_correctly(){

        User manager = TestDataFactory.createSampleManager();
        ComplaintResponse pendingResponse = TestDataFactory.createSamplePendingResponse();
        final String Reson = "Response is not detailed enough.";

        ResponseApprovalDTO rejectionDTO = ResponseApprovalDTO.builder()
                .responseId(pendingResponse.getId())
                .action(ResponseApprovalDTO.ActionEnum.REJECT)
                .rejectionReason(Reson)
                .build();
        when(responseRepository.findById(pendingResponse.getId())).thenReturn(Optional.of(pendingResponse));
            MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class);
            Authentication auth = mock(Authentication.class);
            SecurityContext securityContext = mock(SecurityContext.class);

            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(auth);
            when(auth.isAuthenticated()).thenReturn(true);
            when(auth.getName()).thenReturn(manager.getEmail());
            when(userRepository.findByEmail(manager.getEmail())).thenReturn(Optional.of(manager));

            responseApprovalService.reviewResponse(rejectionDTO);

            verify(responseRepository).save(captor.capture());
            ComplaintResponse savedResponse = captor.getValue();

            assertThat(savedResponse.getStatus()).isEqualTo(ResponseStatus.REJECTED);
            assertThat(savedResponse.getRejectionReason()).isEqualTo(Reson);
            assertThat(savedResponse.getReviewedBy()).isEqualTo(manager);
            assertThat(savedResponse.getReviewedAt()).isNotNull();

            verify(emailService, never()).sendComplaintResponse(anyString(), anyString(), anyString());
    }
}


