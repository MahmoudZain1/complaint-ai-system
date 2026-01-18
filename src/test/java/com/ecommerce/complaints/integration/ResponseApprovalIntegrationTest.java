package com.ecommerce.complaints.integration;

import com.ecommerce.complaints.common.TestAuthUtils;
import com.ecommerce.complaints.model.entity.ComplaintResponse;
import com.ecommerce.complaints.model.enums.ResponseStatus;
import com.ecommerce.complaints.model.generate.AuthResponse;
import com.ecommerce.complaints.model.generate.ResponseApprovalDTO;
import com.ecommerce.complaints.repository.api.ComplaintResponseRepository;
import com.ecommerce.complaints.service.EmailNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Response Approval API Integration Tests")
public class ResponseApprovalIntegrationTest {

    @LocalServerPort
    private int port;

    private RestClient reviewClient;
    private RestClient authClient;

    @Autowired private ComplaintResponseRepository responseRepository;
    @MockBean private EmailNotificationService emailService;

    @BeforeEach
    void setUp() {
        String baseUrl = "http://localhost:" + port;
        reviewClient = RestClient.create(baseUrl + "/complaints/responses");
        authClient = RestClient.create(baseUrl + "/auth");
    }


    @Test
    @Sql(scripts = {
            "classpath:sql/clear-complaint.sql",
            "classpath:sql/clear-user.sql",
            "classpath:sql/insert-test-user.sql",
            "classpath:sql/insert-test-manager.sql",
            "classpath:sql/insert-shipping-complaint.sql",
            "classpath:sql/insert-pending-response.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void reviewResponse_shouldSucceed_whenUserIsManagerAndActionIsApprove(){
        final Long RESPONSE_ID = 100L;

        AuthResponse authResponse = TestAuthUtils.loginAsManager(authClient);
        String authHeaderValue = "Bearer " + authResponse.getAccessToken();
        ResponseApprovalDTO approvalDTO = new ResponseApprovalDTO(RESPONSE_ID, ResponseApprovalDTO.ActionEnum.APPROVE);
        ResponseEntity<Void> responseEntity = reviewClient.post()
                .uri("/review")
                .header(HttpHeaders.AUTHORIZATION, authHeaderValue)
                .contentType(MediaType.APPLICATION_JSON)
                .body(approvalDTO)
                .retrieve()
                .toBodilessEntity();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        ComplaintResponse updatedResponse = responseRepository.findById(RESPONSE_ID).orElseThrow();

        assertThat(updatedResponse.getStatus()).isEqualTo(ResponseStatus.SENT);
        assertThat(updatedResponse.getReviewedBy()).isNotNull();
        assertThat(updatedResponse.getReviewedBy().getId()).isEqualTo(authResponse.getUserId());
        assertThat(updatedResponse.getReviewedAt()).isNotNull();

        verify(emailService, times(1)).sendComplaintResponse(anyString(), anyString(), anyString());


    }

    @Test
    @Sql(scripts = {
            "classpath:sql/clear-complaint.sql",
            "classpath:sql/clear-user.sql",
            "classpath:sql/insert-test-user.sql",
            "classpath:sql/insert-shipping-complaint.sql",
            "classpath:sql/insert-pending-response.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void reviewResponse_shouldFail_whenUserIsCustomer(){
        final Long Response_Id = 100L;
        AuthResponse authResponse = TestAuthUtils.loginAsCustomer(authClient);
        String authHeaderValue = "Bearer " + authResponse.getAccessToken();


        ResponseApprovalDTO approvalDTO = new ResponseApprovalDTO(Response_Id, ResponseApprovalDTO.ActionEnum.APPROVE);
        assertThatThrownBy(() -> {
            reviewClient.post()
                    .uri("/review")
                    .header(HttpHeaders.AUTHORIZATION, authHeaderValue)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(approvalDTO)
                    .retrieve()
                    .toBodilessEntity();
        })
                .isInstanceOf(RestClientResponseException.class)
                .satisfies(ex -> {
                    RestClientResponseException e = (RestClientResponseException) ex;
                    assertThat(e.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
                });
        ComplaintResponse responseAfter = responseRepository.findById(Response_Id).orElseThrow();
        assertThat(responseAfter.getStatus()).isEqualTo(ResponseStatus.PENDING_APPROVAL);

        verify(emailService, never()).sendComplaintResponse(anyString(), anyString(), anyString());
    }


    @Test
    @Sql(scripts = {
            "classpath:sql/clear-complaint.sql",
            "classpath:sql/clear-user.sql",
            "classpath:sql/insert-test-user.sql",
            "classpath:sql/insert-test-manager.sql",
            "classpath:sql/insert-shipping-complaint.sql",
            "classpath:sql/insert-pending-response.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void reviewResponse_shouldSucceed_whenUserIsManagerAndActionIsReject(){
        final Long Response_Id = 100L;
        final String Rejection_Reson ="The generated response is not empathetic enough";

        AuthResponse authResponse = TestAuthUtils.loginAsManager(authClient);
        String authHeaderValue = "Bearer " + authResponse.getAccessToken();

        ResponseApprovalDTO approvalDTO = ResponseApprovalDTO.builder()
                .responseId(Response_Id)
                .action(ResponseApprovalDTO.ActionEnum.REJECT)
                .rejectionReason(Rejection_Reson)
                .build();

        ResponseEntity<Void> responseEntity = reviewClient.post()
                .uri("/review")
                .header(HttpHeaders.AUTHORIZATION, authHeaderValue)
                .contentType(MediaType.APPLICATION_JSON)
                .body(approvalDTO)
                .retrieve()
                .toBodilessEntity();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ComplaintResponse updatedResponse = responseRepository.findById(Response_Id).orElseThrow();

        assertThat(updatedResponse.getStatus()).isEqualTo(ResponseStatus.REJECTED);
        assertThat(updatedResponse.getRejectionReason()).isEqualTo(Rejection_Reson);
        assertThat(updatedResponse.getReviewedBy()).isNotNull();
        assertThat(updatedResponse.getReviewedAt()).isNotNull();

        verify(emailService, never()).sendComplaintResponse(anyString(), anyString(), anyString());

    }

}
