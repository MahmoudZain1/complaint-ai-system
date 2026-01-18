package com.ecommerce.complaints.service;

import com.ecommerce.complaints.common.TestDataFactory;
import com.ecommerce.complaints.exception.BusinessException;
import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.model.entity.User;
import com.ecommerce.complaints.model.error.ComplaintErrors;
import com.ecommerce.complaints.model.generate.ComplaintAnalysisVTO;
import com.ecommerce.complaints.repository.api.ComplaintRepository;
import com.ecommerce.complaints.service.formatter.AnalysisFormatter;
import com.ecommerce.complaints.service.rag.PromptBuilderService;
import com.ecommerce.complaints.service.rag.RAGContextService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Complaint Analysis Service Unit Tests")
public class ComplaintAnalysisServiceImplTest {

    @Mock private ComplaintRepository complaintRepository;
    @Mock private PromptBuilderService promptBuilderService;
    @Mock private AnalysisFormatter analysisFormatter;
    @Mock private RAGContextService ragContextService;
    @Mock private ChatClient chatClient;

    @Captor private ArgumentCaptor<Complaint> complaintCaptor;

    @InjectMocks
    private ComplaintAnalysisServiceImpl complaintAnalysisService;


    @Test
    @SneakyThrows
    void processAiAnalysis_shouldUpdateComplaint_onSuccessfulAnalysis(){
        Complaint existingComplaint = TestDataFactory.createSampleComplaint();
        ComplaintAnalysisVTO fakeAnalysis = TestDataFactory.createSampleAnalysisVTO();

        when(complaintRepository.findById(existingComplaint.getId())).thenReturn(Optional.of(existingComplaint));
        when(ragContextService.getPolicyContextForResponse(anyString())).thenReturn("Some policy context");
        when(promptBuilderService.buildAnalysisPrompt(any(), any(), any(), any())).thenReturn("A complete fake prompt");

        ChatClient.ChatClientRequestSpec requestSpecMock = mock(ChatClient.ChatClientRequestSpec.class);
        ChatClient.CallResponseSpec callResponseSpecMock = mock(ChatClient.CallResponseSpec.class);

        when(chatClient.prompt()).thenReturn(requestSpecMock);
        when(requestSpecMock.user(anyString())).thenReturn(requestSpecMock);
        when(requestSpecMock.call()).thenReturn(callResponseSpecMock);
        when(callResponseSpecMock.entity(ComplaintAnalysisVTO.class)).thenReturn(fakeAnalysis);
        when(analysisFormatter.formatAnalysis(any(ComplaintAnalysisVTO.class))).thenReturn("Formatted analysis text");
        complaintAnalysisService.processAiAnalysis(existingComplaint.getId(), "some content");

        verify(complaintRepository).update(complaintCaptor.capture());
        Complaint updatedComplaint = complaintCaptor.getValue();

        assertThat(updatedComplaint.getCategory()).isEqualTo(fakeAnalysis.getCategory());
        assertThat(updatedComplaint.getPriority()).isEqualTo(fakeAnalysis.getPriority());
        assertThat(updatedComplaint.getSentiment()).isEqualTo(fakeAnalysis.getSentiment());
        assertThat(updatedComplaint.getAiAnalysis()).isEqualTo("Formatted analysis text");


        verify(promptBuilderService).buildAnalysisPrompt(eq(existingComplaint.getSubject()),
                eq(existingComplaint.getDescription()), anyString(), anyString());
    }

    @Test
    @SneakyThrows
    void processAiAnalysis_shouldHandleException_whenChatClientFails(){
        Complaint existingComplaint = TestDataFactory.createSampleComplaint();

        when(complaintRepository.findById(existingComplaint.getId())).thenReturn(Optional.of(existingComplaint));
        when(ragContextService.getPolicyContextForResponse(anyString())).thenReturn("Some policy context");
        when(promptBuilderService.buildAnalysisPrompt(any(), any(), any(), any())).thenReturn("A complete fake prompt");
        when(chatClient.prompt()).thenThrow(new RuntimeException("Simulated API failure"));
        assertThatThrownBy(() -> {
            complaintAnalysisService.processAiAnalysis(existingComplaint.getId(), "some content");
        })
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> {
                    BusinessException be = (BusinessException) ex;
                    assertThat(be.getErrorCode()).isEqualTo(ComplaintErrors.AI_ANALYSIS_FAILED.getCode());
                });

        verify(complaintRepository, never()).update(any(Complaint.class));
    }

}
