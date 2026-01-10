package com.ecommerce.complaints.model.generate;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.annotation.Generated;

/**
 * ComplaintAnalysisVTO
 */
@lombok.experimental.SuperBuilder(toBuilder = true)
@lombok.Data
@lombok.extern.jackson.Jacksonized

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class ComplaintAnalysisVTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long complaintId;

  private com.ecommerce.complaints.model.enums.ComplaintCategory category;

  private Double categoryConfidence;

  private com.ecommerce.complaints.model.enums.Sentiment sentiment;

  private Double sentimentScore;

  private com.ecommerce.complaints.model.enums.Priority priority;

  private Double urgencyScore;

  @Valid
  private List<String> keywords;

  private String summary;

  @Valid
  private List<String> recommendedActions;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime analyzedAt;

  public ComplaintAnalysisVTO complaintId(Long complaintId) {
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

  public ComplaintAnalysisVTO category(com.ecommerce.complaints.model.enums.ComplaintCategory category) {
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

  public ComplaintAnalysisVTO categoryConfidence(Double categoryConfidence) {
    this.categoryConfidence = categoryConfidence;
    return this;
  }

  /**
   * Get categoryConfidence
   * @return categoryConfidence
  */
  
  @Schema(name = "categoryConfidence", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("categoryConfidence")
  public Double getCategoryConfidence() {
    return categoryConfidence;
  }

  public void setCategoryConfidence(Double categoryConfidence) {
    this.categoryConfidence = categoryConfidence;
  }

  public ComplaintAnalysisVTO sentiment(com.ecommerce.complaints.model.enums.Sentiment sentiment) {
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

  public ComplaintAnalysisVTO sentimentScore(Double sentimentScore) {
    this.sentimentScore = sentimentScore;
    return this;
  }

  /**
   * Get sentimentScore
   * @return sentimentScore
  */
  
  @Schema(name = "sentimentScore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sentimentScore")
  public Double getSentimentScore() {
    return sentimentScore;
  }

  public void setSentimentScore(Double sentimentScore) {
    this.sentimentScore = sentimentScore;
  }

  public ComplaintAnalysisVTO priority(com.ecommerce.complaints.model.enums.Priority priority) {
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

  public ComplaintAnalysisVTO urgencyScore(Double urgencyScore) {
    this.urgencyScore = urgencyScore;
    return this;
  }

  /**
   * Get urgencyScore
   * @return urgencyScore
  */
  
  @Schema(name = "urgencyScore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("urgencyScore")
  public Double getUrgencyScore() {
    return urgencyScore;
  }

  public void setUrgencyScore(Double urgencyScore) {
    this.urgencyScore = urgencyScore;
  }

  public ComplaintAnalysisVTO keywords(List<String> keywords) {
    this.keywords = keywords;
    return this;
  }

  public ComplaintAnalysisVTO addKeywordsItem(String keywordsItem) {
    if (this.keywords == null) {
      this.keywords = new ArrayList<>();
    }
    this.keywords.add(keywordsItem);
    return this;
  }

  /**
   * Get keywords
   * @return keywords
  */
  
  @Schema(name = "keywords", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("keywords")
  public List<String> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<String> keywords) {
    this.keywords = keywords;
  }

  public ComplaintAnalysisVTO summary(String summary) {
    this.summary = summary;
    return this;
  }

  /**
   * Get summary
   * @return summary
  */
  
  @Schema(name = "summary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("summary")
  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public ComplaintAnalysisVTO recommendedActions(List<String> recommendedActions) {
    this.recommendedActions = recommendedActions;
    return this;
  }

  public ComplaintAnalysisVTO addRecommendedActionsItem(String recommendedActionsItem) {
    if (this.recommendedActions == null) {
      this.recommendedActions = new ArrayList<>();
    }
    this.recommendedActions.add(recommendedActionsItem);
    return this;
  }

  /**
   * Get recommendedActions
   * @return recommendedActions
  */
  
  @Schema(name = "recommendedActions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendedActions")
  public List<String> getRecommendedActions() {
    return recommendedActions;
  }

  public void setRecommendedActions(List<String> recommendedActions) {
    this.recommendedActions = recommendedActions;
  }

  public ComplaintAnalysisVTO analyzedAt(LocalDateTime analyzedAt) {
    this.analyzedAt = analyzedAt;
    return this;
  }

  /**
   * Get analyzedAt
   * @return analyzedAt
  */
  @Valid 
  @Schema(name = "analyzedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("analyzedAt")
  public LocalDateTime getAnalyzedAt() {
    return analyzedAt;
  }

  public void setAnalyzedAt(LocalDateTime analyzedAt) {
    this.analyzedAt = analyzedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ComplaintAnalysisVTO complaintAnalysisVTO = (ComplaintAnalysisVTO) o;
    return Objects.equals(this.complaintId, complaintAnalysisVTO.complaintId) &&
        Objects.equals(this.category, complaintAnalysisVTO.category) &&
        Objects.equals(this.categoryConfidence, complaintAnalysisVTO.categoryConfidence) &&
        Objects.equals(this.sentiment, complaintAnalysisVTO.sentiment) &&
        Objects.equals(this.sentimentScore, complaintAnalysisVTO.sentimentScore) &&
        Objects.equals(this.priority, complaintAnalysisVTO.priority) &&
        Objects.equals(this.urgencyScore, complaintAnalysisVTO.urgencyScore) &&
        Objects.equals(this.keywords, complaintAnalysisVTO.keywords) &&
        Objects.equals(this.summary, complaintAnalysisVTO.summary) &&
        Objects.equals(this.recommendedActions, complaintAnalysisVTO.recommendedActions) &&
        Objects.equals(this.analyzedAt, complaintAnalysisVTO.analyzedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(complaintId, category, categoryConfidence, sentiment, sentimentScore, priority, urgencyScore, keywords, summary, recommendedActions, analyzedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ComplaintAnalysisVTO {\n");
    sb.append("    complaintId: ").append(toIndentedString(complaintId)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    categoryConfidence: ").append(toIndentedString(categoryConfidence)).append("\n");
    sb.append("    sentiment: ").append(toIndentedString(sentiment)).append("\n");
    sb.append("    sentimentScore: ").append(toIndentedString(sentimentScore)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    urgencyScore: ").append(toIndentedString(urgencyScore)).append("\n");
    sb.append("    keywords: ").append(toIndentedString(keywords)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    recommendedActions: ").append(toIndentedString(recommendedActions)).append("\n");
    sb.append("    analyzedAt: ").append(toIndentedString(analyzedAt)).append("\n");
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

