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
 * ComplaintVTO
 */
@lombok.experimental.SuperBuilder(toBuilder = true)
@lombok.Data
@lombok.extern.jackson.Jacksonized

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class ComplaintVTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private String customerId;

  private String customerName;

  private String customerEmail;

  private String subject;

  private String description;

  private com.ecommerce.complaints.model.enums.ComplaintCategory category;

  private com.ecommerce.complaints.model.enums.ComplaintStatus status;

  private com.ecommerce.complaints.model.enums.Priority priority;

  private com.ecommerce.complaints.model.enums.Sentiment sentiment;

  private String aiAnalysis;

  private String aiGeneratedResponse;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime updatedAt;

  public ComplaintVTO id(Long id) {
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

  public ComplaintVTO customerId(String customerId) {
    this.customerId = customerId;
    return this;
  }

  /**
   * Get customerId
   * @return customerId
  */
  
  @Schema(name = "customerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("customerId")
  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public ComplaintVTO customerName(String customerName) {
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

  public ComplaintVTO customerEmail(String customerEmail) {
    this.customerEmail = customerEmail;
    return this;
  }

  /**
   * Get customerEmail
   * @return customerEmail
  */
  
  @Schema(name = "customerEmail", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("customerEmail")
  public String getCustomerEmail() {
    return customerEmail;
  }

  public void setCustomerEmail(String customerEmail) {
    this.customerEmail = customerEmail;
  }

  public ComplaintVTO subject(String subject) {
    this.subject = subject;
    return this;
  }

  /**
   * Get subject
   * @return subject
  */
  
  @Schema(name = "subject", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subject")
  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public ComplaintVTO description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
  */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ComplaintVTO category(com.ecommerce.complaints.model.enums.ComplaintCategory category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
  */
  @Valid 
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public com.ecommerce.complaints.model.enums.ComplaintCategory getCategory() {
    return category;
  }

  public void setCategory(com.ecommerce.complaints.model.enums.ComplaintCategory category) {
    this.category = category;
  }

  public ComplaintVTO status(com.ecommerce.complaints.model.enums.ComplaintStatus status) {
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
  public com.ecommerce.complaints.model.enums.ComplaintStatus getStatus() {
    return status;
  }

  public void setStatus(com.ecommerce.complaints.model.enums.ComplaintStatus status) {
    this.status = status;
  }

  public ComplaintVTO priority(com.ecommerce.complaints.model.enums.Priority priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
  */
  @Valid 
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public com.ecommerce.complaints.model.enums.Priority getPriority() {
    return priority;
  }

  public void setPriority(com.ecommerce.complaints.model.enums.Priority priority) {
    this.priority = priority;
  }

  public ComplaintVTO sentiment(com.ecommerce.complaints.model.enums.Sentiment sentiment) {
    this.sentiment = sentiment;
    return this;
  }

  /**
   * Get sentiment
   * @return sentiment
  */
  @Valid 
  @Schema(name = "sentiment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sentiment")
  public com.ecommerce.complaints.model.enums.Sentiment getSentiment() {
    return sentiment;
  }

  public void setSentiment(com.ecommerce.complaints.model.enums.Sentiment sentiment) {
    this.sentiment = sentiment;
  }

  public ComplaintVTO aiAnalysis(String aiAnalysis) {
    this.aiAnalysis = aiAnalysis;
    return this;
  }

  /**
   * Get aiAnalysis
   * @return aiAnalysis
  */
  
  @Schema(name = "aiAnalysis", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aiAnalysis")
  public String getAiAnalysis() {
    return aiAnalysis;
  }

  public void setAiAnalysis(String aiAnalysis) {
    this.aiAnalysis = aiAnalysis;
  }

  public ComplaintVTO aiGeneratedResponse(String aiGeneratedResponse) {
    this.aiGeneratedResponse = aiGeneratedResponse;
    return this;
  }

  /**
   * Get aiGeneratedResponse
   * @return aiGeneratedResponse
  */
  
  @Schema(name = "aiGeneratedResponse", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aiGeneratedResponse")
  public String getAiGeneratedResponse() {
    return aiGeneratedResponse;
  }

  public void setAiGeneratedResponse(String aiGeneratedResponse) {
    this.aiGeneratedResponse = aiGeneratedResponse;
  }

  public ComplaintVTO createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
  */
  @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public ComplaintVTO updatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
  */
  @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ComplaintVTO complaintVTO = (ComplaintVTO) o;
    return Objects.equals(this.id, complaintVTO.id) &&
        Objects.equals(this.customerId, complaintVTO.customerId) &&
        Objects.equals(this.customerName, complaintVTO.customerName) &&
        Objects.equals(this.customerEmail, complaintVTO.customerEmail) &&
        Objects.equals(this.subject, complaintVTO.subject) &&
        Objects.equals(this.description, complaintVTO.description) &&
        Objects.equals(this.category, complaintVTO.category) &&
        Objects.equals(this.status, complaintVTO.status) &&
        Objects.equals(this.priority, complaintVTO.priority) &&
        Objects.equals(this.sentiment, complaintVTO.sentiment) &&
        Objects.equals(this.aiAnalysis, complaintVTO.aiAnalysis) &&
        Objects.equals(this.aiGeneratedResponse, complaintVTO.aiGeneratedResponse) &&
        Objects.equals(this.createdAt, complaintVTO.createdAt) &&
        Objects.equals(this.updatedAt, complaintVTO.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, customerId, customerName, customerEmail, subject, description, category, status, priority, sentiment, aiAnalysis, aiGeneratedResponse, createdAt, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ComplaintVTO {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    customerId: ").append(toIndentedString(customerId)).append("\n");
    sb.append("    customerName: ").append(toIndentedString(customerName)).append("\n");
    sb.append("    customerEmail: ").append(toIndentedString(customerEmail)).append("\n");
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    sentiment: ").append(toIndentedString(sentiment)).append("\n");
    sb.append("    aiAnalysis: ").append(toIndentedString(aiAnalysis)).append("\n");
    sb.append("    aiGeneratedResponse: ").append(toIndentedString(aiGeneratedResponse)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

