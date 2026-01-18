package com.ecommerce.complaints.integration;

import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.repository.api.ComplaintRepository;
import com.ecommerce.complaints.service.api.ComplaintAnalysisService;
import lombok.SneakyThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.jdbc.Sql;


import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Tag("live")
@EnabledIfEnvironmentVariable(named = "API_KEY", matches = ".+")
@DisplayName("Complaint Analysis API Integration Tests")
public class ComplaintAnalysisIntegrationTest {

    @Autowired
    private ComplaintAnalysisService complaintAnalysisService;

    @Autowired
    private ComplaintRepository complaintRepository;


    @Test
    @Sql(scripts = {
            "classpath:sql/clear-complaint.sql",
            "classpath:sql/clear-user.sql",
            "classpath:sql/insert-test-user.sql",
            "classpath:sql/insert-shipping-complaint.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @SneakyThrows
  void processAiAnalysis_should_connectToOpenAI_and_updateComplaint(){

      final Long COMPLAINT_ID = 100L;
      Complaint complaintBeforeAnalysis = complaintRepository.findById(COMPLAINT_ID).orElseThrow();

      assertThat(complaintBeforeAnalysis.getCategory()).isNull();
      assertThat(complaintBeforeAnalysis.getPriority()).isNull();
      assertThat(complaintBeforeAnalysis.getSentiment()).isNull();

      complaintAnalysisService.processAiAnalysis(COMPLAINT_ID, complaintBeforeAnalysis.getDescription());
      Optional<Complaint> updatedComplaintOpt = complaintRepository.findById(COMPLAINT_ID);
      assertThat(updatedComplaintOpt).isPresent();
      Complaint updatedComplaint = updatedComplaintOpt.get();

      assertThat(updatedComplaint.getCategory()).isNotNull();
      assertThat(updatedComplaint.getPriority()).isNotNull();
      assertThat(updatedComplaint.getSentiment()).isNotNull();

      assertThat(updatedComplaint.getAiAnalysis()).isNotNull().isNotBlank();
      assertThat(updatedComplaint.getCategory().name()).isEqualTo("SHIPPING");

  }


}
