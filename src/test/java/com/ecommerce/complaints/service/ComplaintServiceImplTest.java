package com.ecommerce.complaints.service;

import com.ecommerce.complaints.common.TestDataFactory;
import com.ecommerce.complaints.exception.BusinessException;
import com.ecommerce.complaints.messaging.api.RabbitMQEventPublisher;
import com.ecommerce.complaints.model.dto.ComplaintSearchRequest;
import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.model.entity.User;
import com.ecommerce.complaints.model.error.ComplaintErrors;
import com.ecommerce.complaints.model.generate.ComplaintCreateDTO;
import com.ecommerce.complaints.repository.api.ComplaintRepository;
import com.ecommerce.complaints.repository.api.UserRepository;
import com.ecommerce.complaints.service.mapper.ComplaintMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("Complaint Service Unit Tests")
public class ComplaintServiceImplTest {

    @Mock private ComplaintRepository complaintRepository;
    @Mock private ComplaintMapper complaintMapper;
    @Mock private RabbitMQEventPublisher eventPublisher;
    @Mock private UserRepository userRepository;
    @Mock private SecurityContext securityContext;
    @Mock private Authentication authentication;

    @Captor private ArgumentCaptor<Pageable> pageableCaptor;
    @Captor private ArgumentCaptor<Complaint> complaintCaptor;


    @InjectMocks
    private ComplaintServiceImpl complaintService;



    @Test
    void createComplaint_shouldSucceed_withValidData(){

        User sampleUser = TestDataFactory.createSampleUser();
        ComplaintCreateDTO createDTO = TestDataFactory.createSampleComplaintDTO();
        Complaint complaintToSave = new Complaint();
        Complaint savedComplaint = TestDataFactory.createSampleComplaint();

        MockedStatic<SecurityContextHolder> mockedContext = mockStatic(SecurityContextHolder.class);
            mockedContext.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.isAuthenticated()).thenReturn(true);
            when(authentication.getName()).thenReturn(sampleUser.getEmail());
            when(userRepository.findByEmail(sampleUser.getEmail())).thenReturn(Optional.of(sampleUser));

            when(complaintMapper.toEntity(createDTO)).thenReturn(complaintToSave);
            when(complaintRepository.save(complaintToSave)).thenReturn(savedComplaint);


            complaintService.createComplaint(createDTO);

            verify(complaintRepository).save(complaintCaptor.capture());
            Complaint capturedComplaint = complaintCaptor.getValue();
            assertThat(capturedComplaint.getCustomer()).isEqualTo(sampleUser);
            assertThat(capturedComplaint.getCustomerName()).isEqualTo(sampleUser.getName());

            verify(eventPublisher).publishEvent(any(), any());

    }

    @Test
    void getComplaintById_shouldThrowException_whenComplaintNotFound(){
        when(complaintRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> complaintService.getComplaintById(99999L))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ComplaintErrors.COMPLAINT_NOT_FOUND.getCode());
    }

    @Test
    void deleteComplaint_shouldThrowException_whenComplaintNotFound(){
        when(complaintRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> complaintService.deleteComplaint(999L))
                .isInstanceOf(BusinessException.class);
        verify(complaintRepository, never()).delete(anyLong());
    }

    @Test
    void listComplaints_shouldUseDefaults_whenRequestIsEmpty(){
        when(complaintRepository.findAll(any(), any(), any(), pageableCaptor.capture()))
                .thenReturn(Page.empty());
        ComplaintSearchRequest emptyRequest = new ComplaintSearchRequest(null,  null, null, null, null, null, null);

        complaintService.listComplaints(emptyRequest);
        Pageable capturedPageable = pageableCaptor.getValue();
        assertThat(capturedPageable.getPageNumber()).isEqualTo(0);
        assertThat(capturedPageable.getPageSize()).isEqualTo(20);
        assertThat(capturedPageable.getSort().getOrderFor("createdAt").getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void listComplaints_shouldUseCustomValues_whenRequestIsProvided(){
        when(complaintRepository.findAll(any(), any(), any(), pageableCaptor.capture()))
                .thenReturn(Page.empty());
        ComplaintSearchRequest customRequest = new ComplaintSearchRequest(null, null, null, 2, 50, "status", "asc");

        complaintService.listComplaints(customRequest);
        Pageable capturedPageable = pageableCaptor.getValue();
        assertThat(capturedPageable.getPageNumber()).isEqualTo(2);
        assertThat(capturedPageable.getPageSize()).isEqualTo(50);
        assertThat(capturedPageable.getSort().getOrderFor("status").getDirection()).isEqualTo(Sort.Direction.ASC);
    }

    @Test
    void listComplaints_shouldFallbackToSort_whenFieldIsInvalid(){
        when(complaintRepository.findAll(any(), any(),  any(), pageableCaptor.capture()))
                .thenReturn(Page.empty());
        ComplaintSearchRequest invalidSortRequest = new ComplaintSearchRequest( null, null, null, 0, 10, "invalidField", "asc");
        complaintService.listComplaints(invalidSortRequest);

        Pageable capturedPageable = pageableCaptor.getValue();
        Sort capturedSort = capturedPageable.getSort();
        assertThat(capturedSort.getOrderFor("invalidField")).isNull();
        assertThat(capturedSort.getOrderFor("createdAt")).isNotNull();
    }

}