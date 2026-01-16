package com.ecommerce.complaints.integration;

import com.ecommerce.complaints.common.TestAuthUtils;
import com.ecommerce.complaints.common.TestDataFactory;
import com.ecommerce.complaints.messaging.api.RabbitMQEventPublisher;
import com.ecommerce.complaints.model.generate.AuthResponse;
import com.ecommerce.complaints.model.generate.ComplaintCreateDTO;
import com.ecommerce.complaints.model.generate.ComplaintVTO;
import com.ecommerce.complaints.repository.api.ComplaintRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.ecommerce.complaints.messaging.event.ComplaintEvent.COMPLAINT_CREATED;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Complaint API Integration Tests")
public class ComplaintIntegrationTest {

    @LocalServerPort
    private int port;

    private RestClient complaintClient;
    private RestClient authClient;

    @MockBean
    private RabbitMQEventPublisher eventPublisher;

    @Autowired
    private ComplaintRepository complaintRepository;

    @BeforeEach
    void setUp() {
        String baseUrl = "http://localhost:" + port;
        complaintClient = RestClient.create(baseUrl);
        authClient = RestClient.create(baseUrl + "/auth");
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/clear-complaint.sql",
            "classpath:sql/clear-user.sql",
            "classpath:sql/insert-test-user.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void createComplaint_shouldSucceed_forAuthenticatedUser(){

        AuthResponse authResponse = TestAuthUtils.loginAsCustomer(authClient);
        String authHeaderValue = "Bearer " + authResponse.getAccessToken();
        ComplaintCreateDTO createDTO = TestDataFactory.createSampleComplaintDTO();

        ResponseEntity<ComplaintVTO> responseEntity = complaintClient.post()
                .uri("/complaints")
                .header(HttpHeaders.AUTHORIZATION , authHeaderValue)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createDTO)
                .retrieve()
                .toEntity(ComplaintVTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ComplaintVTO responseVTO = responseEntity.getBody();
        assertThat(responseVTO).isNotNull();
        assertThat(responseVTO.getId()).isPositive();
        assertThat(responseVTO.getSubject()).isEqualTo("Product arrived damaged");
        assertThat(complaintRepository.count()).isEqualTo(1);
        verify(eventPublisher).publishEvent(eq(COMPLAINT_CREATED), any());
    }

    @Sql(scripts = "classpath:sql/clear-complaint.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @ParameterizedTest(name = "Test with invalid header: {0}")
    @ValueSource(strings = {
            "",
            "Bearer ",
            "Bearer invalid-token",
            "WrongScheme some-token"
    })
    void createComplaint_shouldFail_forInvalidAuthenticatio(String invalidAuthHeader){
        ComplaintCreateDTO createDTO = TestDataFactory.createSampleComplaintDTO();

        assertThatThrownBy(() -> {
            complaintClient.post()
                    .uri("/complaints")
                    .header(HttpHeaders.AUTHORIZATION, invalidAuthHeader)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(createDTO)
                    .retrieve()
                    .toBodilessEntity();
        })
                .isInstanceOf(RestClientResponseException.class)
                .satisfies(ex -> {
                    RestClientResponseException e = (RestClientResponseException) ex;
                    assertThat(e.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
                });

        assertThat(complaintRepository.count()).isZero();
    }

    @Test
    @Sql(scripts = {
            "classpath:sql/clear-complaint.sql",
            "classpath:sql/clear-user.sql",
            "classpath:sql/insert-test-user.sql",
            "classpath:sql/insert-test-complaint.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteComplaint_shouldFail_whenUserIsCustomer(){
        AuthResponse authResponse = TestAuthUtils.loginAsCustomer(authClient);
        String authHeaderValue = "Bearer " + authResponse.getAccessToken();

        assertThatThrownBy(() -> {
            complaintClient.delete()
                    .uri("/complaints/100")
                    .header(HttpHeaders.AUTHORIZATION, authHeaderValue)
                    .retrieve()
                    .toBodilessEntity();
        })
                .isInstanceOf(RestClientResponseException.class)
                .satisfies(ex -> {
                    RestClientResponseException e = (RestClientResponseException) ex;
                    assertThat(e.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
                });

        assertThat(complaintRepository.count()).isEqualTo(1);

    }


    @Test
    @Sql(scripts = {
            "classpath:sql/clear-complaint.sql",
            "classpath:sql/clear-user.sql",
            "classpath:sql/insert-test-user.sql",
            "classpath:sql/insert-test-manager.sql",
            "classpath:sql/insert-test-complaint.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteComplaint_shouldSucceed_whenUserIsManager(){
        AuthResponse authResponse = TestAuthUtils.loginAsManager(authClient);
        String authHeaderValue = "Bearer " + authResponse.getAccessToken();

        ResponseEntity<Void> responseEntity = complaintClient.delete()
                .uri("/complaints/100")
                .header(HttpHeaders.AUTHORIZATION, authHeaderValue)
                .retrieve()
                .toBodilessEntity();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(complaintRepository.count()).isZero();
    }
}
