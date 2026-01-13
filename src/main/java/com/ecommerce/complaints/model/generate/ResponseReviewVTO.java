package com.ecommerce.complaints.model.generate;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ResponseReviewVTO
 */
@lombok.experimental.SuperBuilder(toBuilder = true)
@lombok.Data
@lombok.extern.jackson.Jacksonized

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class ResponseReviewVTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private Long complaintId;

  private String complaintSubject;

  private String customerName;

  private String generatedResponse;

  private String tone;

  private Double confidenceScore;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime generatedAt;

  private com.ecommerce.complaints.model.enums.ResponseStatus status;

  private String priority;

  public ResponseReviewVTO id(Long id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
  */
  
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ResponseReviewVTO complaintId(Long complaintId) {
    this.complaintId = complaintId;
    return this;
  }

  /**
   * Get complaintId
   * @return complaintId
  */
  
  @Schema(name = "complaintId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("complaintId")
  public Long getComplaintId() {
    return complaintId;
  }

  public void setComplaintId(Long complaintId) {
    this.complaintId = complaintId;
  }

  public ResponseReviewVTO complaintSubject(String complaintSubject) {
    this.complaintSubject = complaintSubject;
    return this;
  }

  /**
   * Get complaintSubject
   * @return complaintSubject
  */
  
  @Schema(name = "complaintSubject", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("complaintSubject")
  public String getComplaintSubject() {
    return complaintSubject;
  }

  public void setComplaintSubject(String complaintSubject) {
    this.complaintSubject = complaintSubject;
  }

  public ResponseReviewVTO customerName(String customerName) {
    this.customerName = customerName;
    return this;
  }

  /**
   * Get customerName
   * @return customerName
  */
  
  @Schema(name = "customerName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("customerName")
  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public ResponseReviewVTO generatedResponse(String generatedResponse) {
    this.generatedResponse = generatedResponse;
    return this;
  }

  /**
   * Get generatedResponse
   * @return generatedResponse
  */
  
  @Schema(name = "generatedResponse", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("generatedResponse")
  public String getGeneratedResponse() {
    return generatedResponse;
  }

  public void setGeneratedResponse(String generatedResponse) {
    this.generatedResponse = generatedResponse;
  }

  public ResponseReviewVTO tone(String tone) {
    this.tone = tone;
    return this;
  }

  /**
   * Get tone
   * @return tone
  */
  
  @Schema(name = "tone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tone")
  public String getTone() {
    return tone;
  }

  public void setTone(String tone) {
    this.tone = tone;
  }

  public ResponseReviewVTO confidenceScore(Double confidenceScore) {
    this.confidenceScore = confidenceScore;
    return this;
  }

  /**
   * Get confidenceScore
   * @return confidenceScore
  */
  
  @Schema(name = "confidenceScore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("confidenceScore")
  public Double getConfidenceScore() {
    return confidenceScore;
  }

  public void setConfidenceScore(Double confidenceScore) {
    this.confidenceScore = confidenceScore;
  }

  public ResponseReviewVTO generatedAt(LocalDateTime generatedAt) {
    this.generatedAt = generatedAt;
    return this;
  }

  /**
   * Get generatedAt
   * @return generatedAt
  */
  @Valid 
  @Schema(name = "generatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("generatedAt")
  public LocalDateTime getGeneratedAt() {
    return generatedAt;
  }

  public void setGeneratedAt(LocalDateTime generatedAt) {
    this.generatedAt = generatedAt;
  }

  public ResponseReviewVTO status(com.ecommerce.complaints.model.enums.ResponseStatus status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  */
  @Valid 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public com.ecommerce.complaints.model.enums.ResponseStatus getStatus() {
    return status;
  }

  public void setStatus(com.ecommerce.complaints.model.enums.ResponseStatus status) {
    this.status = status;
  }

  public ResponseReviewVTO priority(String priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
  */
  
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseReviewVTO responseReviewVTO = (ResponseReviewVTO) o;
    return Objects.equals(this.id, responseReviewVTO.id) &&
        Objects.equals(this.complaintId, responseReviewVTO.complaintId) &&
        Objects.equals(this.complaintSubject, responseReviewVTO.complaintSubject) &&
        Objects.equals(this.customerName, responseReviewVTO.customerName) &&
        Objects.equals(this.generatedResponse, responseReviewVTO.generatedResponse) &&
        Objects.equals(this.tone, responseReviewVTO.tone) &&
        Objects.equals(this.confidenceScore, responseReviewVTO.confidenceScore) &&
        Objects.equals(this.generatedAt, responseReviewVTO.generatedAt) &&
        Objects.equals(this.status, responseReviewVTO.status) &&
        Objects.equals(this.priority, responseReviewVTO.priority);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, complaintId, complaintSubject, customerName, generatedResponse, tone, confidenceScore, generatedAt, status, priority);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResponseReviewVTO {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    complaintId: ").append(toIndentedString(complaintId)).append("\n");
    sb.append("    complaintSubject: ").append(toIndentedString(complaintSubject)).append("\n");
    sb.append("    customerName: ").append(toIndentedString(customerName)).append("\n");
    sb.append("    generatedResponse: ").append(toIndentedString(generatedResponse)).append("\n");
    sb.append("    tone: ").append(toIndentedString(tone)).append("\n");
    sb.append("    confidenceScore: ").append(toIndentedString(confidenceScore)).append("\n");
    sb.append("    generatedAt: ").append(toIndentedString(generatedAt)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

