package com.ecommerce.complaints.model.vto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ComplaintResponseVTO
 */
@lombok.experimental.SuperBuilder(toBuilder = true)
@lombok.Data
@lombok.extern.jackson.Jacksonized

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class ComplaintResponseVTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long complaintId;

  private String generatedResponse;

  private String tone;

  @Valid
  private List<String> suggestedActions;

  private Double confidenceScore;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime generatedAt;

  public ComplaintResponseVTO complaintId(Long complaintId) {
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

  public ComplaintResponseVTO generatedResponse(String generatedResponse) {
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

  public ComplaintResponseVTO tone(String tone) {
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

  public ComplaintResponseVTO suggestedActions(List<String> suggestedActions) {
    this.suggestedActions = suggestedActions;
    return this;
  }

  public ComplaintResponseVTO addSuggestedActionsItem(String suggestedActionsItem) {
    if (this.suggestedActions == null) {
      this.suggestedActions = new ArrayList<>();
    }
    this.suggestedActions.add(suggestedActionsItem);
    return this;
  }

  /**
   * Get suggestedActions
   * @return suggestedActions
  */
  
  @Schema(name = "suggestedActions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("suggestedActions")
  public List<String> getSuggestedActions() {
    return suggestedActions;
  }

  public void setSuggestedActions(List<String> suggestedActions) {
    this.suggestedActions = suggestedActions;
  }

  public ComplaintResponseVTO confidenceScore(Double confidenceScore) {
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

  public ComplaintResponseVTO generatedAt(LocalDateTime generatedAt) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ComplaintResponseVTO complaintResponseVTO = (ComplaintResponseVTO) o;
    return Objects.equals(this.complaintId, complaintResponseVTO.complaintId) &&
        Objects.equals(this.generatedResponse, complaintResponseVTO.generatedResponse) &&
        Objects.equals(this.tone, complaintResponseVTO.tone) &&
        Objects.equals(this.suggestedActions, complaintResponseVTO.suggestedActions) &&
        Objects.equals(this.confidenceScore, complaintResponseVTO.confidenceScore) &&
        Objects.equals(this.generatedAt, complaintResponseVTO.generatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(complaintId, generatedResponse, tone, suggestedActions, confidenceScore, generatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ComplaintResponseVTO {\n");
    sb.append("    complaintId: ").append(toIndentedString(complaintId)).append("\n");
    sb.append("    generatedResponse: ").append(toIndentedString(generatedResponse)).append("\n");
    sb.append("    tone: ").append(toIndentedString(tone)).append("\n");
    sb.append("    suggestedActions: ").append(toIndentedString(suggestedActions)).append("\n");
    sb.append("    confidenceScore: ").append(toIndentedString(confidenceScore)).append("\n");
    sb.append("    generatedAt: ").append(toIndentedString(generatedAt)).append("\n");
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

