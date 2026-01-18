package com.ecommerce.complaints.integration;


import com.ecommerce.complaints.model.entity.ComplaintResponse;
import com.ecommerce.complaints.repository.api.ComplaintResponseRepository;
import com.ecommerce.complaints.service.api.ComplaintResponseService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@Tag("live")
@EnabledIfEnvironmentVariable(named = "API_KEY", matches = ".+")
@DisplayName("Complaint Response Service  Integration Tests")
public class ComplaintResponIntegrationTest {

    @Autowired
    private ComplaintResponseService complaintResponseService;

    @Autowired
    private ComplaintResponseRepository complaintResponseRepository;


    @Test
    @Sql(scripts = {
            "classpath:sql/clear-complaint.sql",
            "classpath:sql/clear-user.sql",
            "classpath:sql/insert-test-user.sql",
            "classpath:sql/insert-shipping-complaint.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void generateResponse_shouldSucceed_inLiveScenario(){
        final Long COMPLAINT_ID = 100L;
        assertThat(complaintResponseRepository.count()).isZero();
        complaintResponseService.generateResponse(COMPLAINT_ID);
        assertThat(complaintResponseRepository.count()).isEqualTo(1);
        ComplaintResponse savedResponse = complaintResponseRepository.findAll().get(0);
        assertThat(savedResponse.getComplaint().getId()).isEqualTo(COMPLAINT_ID);
        assertThat(savedResponse.getGeneratedResponse()).isNotNull().isNotBlank();
        assertThat(savedResponse.getStatus().name()).isEqualTo("PENDING_APPROVAL");
    }

}
