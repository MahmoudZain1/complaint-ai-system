package com.ecommerce.complaints.integration.service;

import com.ecommerce.complaints.integration.common.RestClientTestUtility;
import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.model.enums.ComplaintStatus;
import com.ecommerce.complaints.model.generate.*;
import com.ecommerce.complaints.repository.api.ComplaintRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestClient;

import static com.ecommerce.complaints.integration.common.TestData.*;
import static com.ecommerce.complaints.model.error.ComplaintErrors.*;
import static com.ecommerce.complaints.model.error.UserErrors.INVALID_EMAIL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ComplaintServiceIntegrationTest {

    private final String API_PATH = "/complaints";

    @LocalServerPort
    private int port;

    @Autowired
    private ComplaintRepository complaintRepository;

    private RestClient COMPLAINT_REST_CLIENT;

    @BeforeEach
    public void setup() {
        COMPLAINT_REST_CLIENT = RestClient.create("http://localhost:" + port);
    }

    @Test
    @Sql(scripts = "/sql/clear-complaint.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testCreateComplaint_Success() {
        ComplaintCreateDTO request = ComplaintCreateDTO.builder()
                .subject(TEST_SUBJECT)
                .description(TEST_DESCRIPTION)
                .build();

        ComplaintVTO response = COMPLAINT_REST_CLIENT.post()
                .uri(builder -> builder.path(API_PATH).build())
                .body(request)
                .retrieve()
                .body(ComplaintVTO.class);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(TEST_EMAIL_1, response.getCustomerEmail());
        assertEquals(TEST_SUBJECT, response.getSubject());
        assertEquals(ComplaintStatus.NEW, response.getStatus());

        Complaint after = complaintRepository.findById(response.getId()).orElseThrow();
        assertNotNull(after);
        assertEquals(TEST_EMAIL_1, after.getCustomerEmail());
        assertEquals(TEST_CUSTOMER_NAME, after.getCustomerName());
    }


    @Test
    @Sql(scripts = "/sql/clear-complaint.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testCreateComplaint_InvalidEmail() {
        ComplaintCreateDTO request = ComplaintCreateDTO.builder()
                .subject(TEST_SUBJECT)
                .description(TEST_DESCRIPTION)
                .build();

        COMPLAINT_REST_CLIENT.post()
                .uri(builder -> builder.path(API_PATH).build())
                .body(request)
                .retrieve()
                .onStatus(status -> status == BAD_REQUEST, (req, res) -> {
                    ErrorVTO error = RestClientTestUtility.readErrorVTO(res);
                    assertNotNull(error);
                    assertEquals(INVALID_EMAIL.name(), error.getError());
                })
                .toBodilessEntity();
    }


    @Test
    @Sql(scripts = "/sql/clear-complaint.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testCreateComplaint_MissingRequiredFields() {
        ComplaintCreateDTO request = ComplaintCreateDTO.builder()
                .subject("")
                .description(TEST_DESCRIPTION)
                .build();

        COMPLAINT_REST_CLIENT.post()
                .uri(builder -> builder.path(API_PATH).build())
                .body(request)
                .retrieve()
                .onStatus(status -> status == BAD_REQUEST, (req, res) -> {
                    ErrorVTO error = RestClientTestUtility.readErrorVTO(res);
                    assertNotNull(error);
                    assertEquals(VALIDATION_ERROR.getCode(), error.getError());
                })
                .toBodilessEntity();
    }

    @Test
    @Sql(scripts = "/sql/clear-complaint.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testCreateComplaint_DescriptionTooLong() {
        String longDescription = "a".repeat(5001);

        ComplaintCreateDTO request = ComplaintCreateDTO.builder()
                .subject(TEST_SUBJECT)
                .description(longDescription)
                .build();

        COMPLAINT_REST_CLIENT.post()
                .uri(builder -> builder.path(API_PATH).build())
                .body(request)
                .retrieve()
                .onStatus(status -> status == BAD_REQUEST, (req, res) -> {
                    ErrorVTO error = RestClientTestUtility.readErrorVTO(res);
                    assertNotNull(error);
                    assertEquals(VALIDATION_ERROR.getCode(), error.getError());
                })
                .toBodilessEntity();
    }

    @Test
    @Sql(scripts = "/sql/clear-complaint.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/insert-complaint.sql")
    void testGetComplaint_Success() {
        Long complaintId = 1L;

        ComplaintVTO response = COMPLAINT_REST_CLIENT.get()
                .uri(builder -> builder.path(API_PATH + "/{id}").build(complaintId))
                .retrieve()
                .body(ComplaintVTO.class);

        assertNotNull(response);
        assertEquals(complaintId, response.getId());
        assertNotNull(response.getCustomerEmail());
        assertNotNull(response.getSubject());
    }

    @Test
    @Sql(scripts = "/sql/clear-complaint.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testGetComplaint_NotFound() {
        Long nonExistentId = 999L;

        COMPLAINT_REST_CLIENT.get()
                .uri(builder -> builder.path(API_PATH + "/{id}").build(nonExistentId))
                .retrieve()
                .onStatus(status -> status == NOT_FOUND, (req, res) -> {
                    ErrorVTO error = RestClientTestUtility.readErrorVTO(res);
                    assertNotNull(error);
                    assertEquals(COMPLAINT_NOT_FOUND.getCode(), error.getError());
                })
                .toBodilessEntity();
    }

    @Test
    @Sql(scripts = "/sql/clear-complaint.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/insert-complaint.sql")
    void testUpdateComplaint_Success() {
        Long complaintId = 1L;

        ComplaintUpdateDTO updateRequest = ComplaintUpdateDTO.builder()
                .subject("Updated Subject")
                .description("Updated Description")
                .build();

        ComplaintVTO response = COMPLAINT_REST_CLIENT.put()
                .uri(builder -> builder.path(API_PATH + "/{id}").build(complaintId))
                .body(updateRequest)
                .retrieve()
                .body(ComplaintVTO.class);

        assertNotNull(response);
        assertEquals(complaintId, response.getId());
        assertEquals("Updated Subject", response.getSubject());
        assertEquals("Updated Description", response.getDescription());

        Complaint after = complaintRepository.findById(complaintId).orElseThrow();
        assertEquals("Updated Subject", after.getSubject());
        assertEquals("Updated Description", after.getDescription());
    }

    @Test
    @Sql(scripts = "/sql/clear-complaint.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/insert-complaint.sql")
    void testUpdateComplaint_NotFound() {
        Long nonExistentId = 999L;

        ComplaintUpdateDTO updateRequest = ComplaintUpdateDTO.builder()
                .subject("Updated Subject")
                .build();

        COMPLAINT_REST_CLIENT.put()
                .uri(builder -> builder.path(API_PATH + "/{id}").build(nonExistentId))
                .body(updateRequest)
                .retrieve()
                .onStatus(status -> status == NOT_FOUND, (req, res) -> {
                    ErrorVTO error = RestClientTestUtility.readErrorVTO(res);
                    assertNotNull(error);
                    assertEquals(COMPLAINT_NOT_FOUND.getCode(), error.getError());
                })
                .toBodilessEntity();
    }

    @Test
    @Sql(scripts = "/sql/clear-complaint.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/insert-complaint.sql")
    void testDeleteComplaint_Success() {
        Long complaintId = 1L;

        COMPLAINT_REST_CLIENT.delete()
                .uri(builder -> builder.path(API_PATH + "/{id}").build(complaintId))
                .retrieve()
                .toBodilessEntity();

        COMPLAINT_REST_CLIENT.get()
                .uri(builder -> builder.path(API_PATH + "/{id}").build(complaintId))
                .retrieve()
                .onStatus(status -> status == NOT_FOUND, (req, res) -> {
                    ErrorVTO error = RestClientTestUtility.readErrorVTO(res);
                    assertNotNull(error);
                    assertEquals(COMPLAINT_NOT_FOUND.getCode(), error.getError());
                })
                .toBodilessEntity();
    }

    @Test
    @Sql(scripts = "/sql/clear-complaint.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testDeleteComplaint_NotFound() {
        Long nonExistentId = 999L;

        COMPLAINT_REST_CLIENT.delete()
                .uri(builder -> builder.path(API_PATH + "/{id}").build(nonExistentId))
                .retrieve()
                .onStatus(status -> status == NOT_FOUND, (req, res) -> {
                    ErrorVTO error = RestClientTestUtility.readErrorVTO(res);
                    assertNotNull(error);
                    assertEquals(COMPLAINT_NOT_FOUND.getCode(), error.getError());
                })
                .toBodilessEntity();
    }



}
