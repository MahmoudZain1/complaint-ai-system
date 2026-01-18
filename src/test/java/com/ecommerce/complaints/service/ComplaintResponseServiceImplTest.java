package com.ecommerce.complaints.service;

import com.ecommerce.complaints.common.TestDataFactory;
import com.ecommerce.complaints.exception.BusinessException;
import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.model.entity.ComplaintResponse;
import com.ecommerce.complaints.model.enums.ResponseStatus;
import com.ecommerce.complaints.model.error.ComplaintErrors;
import com.ecommerce.complaints.model.generate.ComplaintResponseVTO;
import com.ecommerce.complaints.repository.api.ComplaintRepository;
import com.ecommerce.complaints.repository.api.ComplaintResponseRepository;
import com.ecommerce.complaints.service.api.ComplaintResponseService;
import com.ecommerce.complaints.service.api.ComplaintService;
import com.ecommerce.complaints.service.mapper.ComplaintMapper;
import com.ecommerce.complaints.service.rag.PromptBuilderService;
import com.ecommerce.complaints.service.rag.RAGContextService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Complaint Response Service Unit Tests")
public class ComplaintResponseServiceImplTest {

    @Mock private ComplaintResponseRepository complaintResponseRepository;
    @Mock private ComplaintRepository complaintRepository;
    @Mock private ChatClient chatClient;
    @Mock private ComplaintMapper complaintMapper;
    @Mock private PromptBuilderService promptBuilderService;
    @Mock private RAGContextService ragContextService;
    @Mock private ResponseApprovalService responseApprovalService;

    @InjectMocks
    private ComplaintResponseServiceImpl complaintResponseService;

    @Test
    void generateResponse_shouldThrowException_whenResponseAlreadyExists(){
        Long COMPLAINT_ID = 100L;
        when(complaintRepository.findById(COMPLAINT_ID))
                .thenReturn(Optional.of(TestDataFactory.createSampleComplaint()));
        when(complaintResponseRepository.existsByComplaintId(COMPLAINT_ID)).thenReturn(true);
        assertThatThrownBy(() -> complaintResponseService.generateResponse(COMPLAINT_ID))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ComplaintErrors.RESPONSE_ALREADY_EXISTS.getCode());
        verify(chatClient, never()).prompt();
    }

    @Test
    @SneakyThrows
    void  generateResponse_shouldSucceed_withMockedAi(){
        Long COMPLAINT_ID = 100L;
        Complaint sampleComplaint = TestDataFactory.createSampleComplaint();
        var fakeResponseVTO = TestDataFactory.createSampleResponseVTO();
        var responseEntity = new ComplaintResponse();


        when(complaintRepository.findById(COMPLAINT_ID)).thenReturn(Optional.of(sampleComplaint));
        when(complaintResponseRepository.existsByComplaintId(COMPLAINT_ID)).thenReturn(false);

        when(ragContextService.retrievePolicyContext(any())).thenReturn(Collections.emptyList());
        when(ragContextService.retrieveSimilarComplaints(any())).thenReturn(Collections.emptyList());
        when(promptBuilderService.buildResponsePrompt(any(), any(), any(), any(), any(), any())).thenReturn("F Prompt");

        ChatClient.ChatClientRequestSpec requestSpecMock = mock(ChatClient.ChatClientRequestSpec.class);
        ChatClient.CallResponseSpec callResponseSpecMock = mock(ChatClient.CallResponseSpec.class);
        when(chatClient.prompt()).thenReturn(requestSpecMock);
        when(requestSpecMock.user(anyString())).thenReturn(requestSpecMock);
        when(requestSpecMock.call()).thenReturn(callResponseSpecMock);
        when(callResponseSpecMock.entity(ComplaintResponseVTO.class)).thenReturn(fakeResponseVTO);

        when(complaintMapper.toEntity(fakeResponseVTO)).thenReturn(responseEntity);
        when(complaintResponseRepository.save(responseEntity)).thenReturn(responseEntity);

        doNothing().when(responseApprovalService).processGeneratedResponse(any());

        ComplaintResponseVTO actualResponse = complaintResponseService.generateResponse(COMPLAINT_ID);

        ArgumentCaptor<ComplaintResponse> captor = ArgumentCaptor.forClass(ComplaintResponse.class);
        verify(complaintResponseRepository).save(captor.capture());
        ComplaintResponse savedEntity = captor.getValue();

        assertThat(savedEntity.getComplaint()).isEqualTo(sampleComplaint);
        assertThat(savedEntity.getStatus()).isEqualTo(ResponseStatus.PENDING_APPROVAL);
        assertThat(savedEntity.getGeneratedAt()).isNotNull();

        verify(responseApprovalService).processGeneratedResponse(savedEntity);

        assertThat(actualResponse).isEqualTo(fakeResponseVTO);

    }

    @Test
    void generateResponse_shouldThrowException_whenComplaintNotFound(){
        final Long NON_EXISTENT_ID = 999L;
        when(complaintRepository.findById(NON_EXISTENT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> complaintResponseService.generateResponse(NON_EXISTENT_ID))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ComplaintErrors.COMPLAINT_NOT_FOUND.getCode());
        verify(complaintResponseRepository, never()).existsByComplaintId(any());
        verify(chatClient, never()).prompt();
    }

    @Test
    @SneakyThrows
    void generateResponse_shouldThrowException_whenChatClientFails(){
        final Long COMPLAINT_ID = 100L;
        when(complaintRepository.findById(COMPLAINT_ID)).thenReturn(Optional.of(TestDataFactory.createSampleComplaint()));
        when(complaintResponseRepository.existsByComplaintId(COMPLAINT_ID)).thenReturn(false);
        when(ragContextService.retrievePolicyContext(any())).thenReturn(Collections.emptyList());
        when(ragContextService.retrieveSimilarComplaints(any())).thenReturn(Collections.emptyList());
        when(promptBuilderService.buildResponsePrompt(any(), any(), any(), any(), any(), any())).thenReturn("F Prompt");

        when(chatClient.prompt()).thenThrow(new RuntimeException("Simulated API failure"));

        assertThatThrownBy(() -> complaintResponseService.generateResponse(COMPLAINT_ID))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Simulated API failure");

        verify(complaintResponseRepository, never()).save(any());
    }
}
