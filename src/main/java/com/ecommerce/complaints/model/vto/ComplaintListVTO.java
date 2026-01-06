package com.ecommerce.complaints.model.vto;

import java.net.URI;
import java.util.Objects;
import com.ecommerce.complaints.model.vto.ComplaintVTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ComplaintListVTO
 */
@lombok.experimental.SuperBuilder(toBuilder = true)
@lombok.Data
@lombok.extern.jackson.Jacksonized

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class ComplaintListVTO implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<@Valid ComplaintVTO> complaints;

  private Long totalElements;

  private Integer totalPages;

  private Integer currentPage;

  private Integer pageSize;

  private Boolean hasNext;

  private Boolean hasPrevious;

  public ComplaintListVTO complaints(List<@Valid ComplaintVTO> complaints) {
    this.complaints = complaints;
    return this;
  }

  public ComplaintListVTO addComplaintsItem(ComplaintVTO complaintsItem) {
    if (this.complaints == null) {
      this.complaints = new ArrayList<>();
    }
    this.complaints.add(complaintsItem);
    return this;
  }

  /**
   * Get complaints
   * @return complaints
  */
  @Valid 
  @Schema(name = "complaints", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("complaints")
  public List<@Valid ComplaintVTO> getComplaints() {
    return complaints;
  }

  public void setComplaints(List<@Valid ComplaintVTO> complaints) {
    this.complaints = complaints;
  }

  public ComplaintListVTO totalElements(Long totalElements) {
    this.totalElements = totalElements;
    return this;
  }

  /**
   * Get totalElements
   * @return totalElements
  */
  
  @Schema(name = "totalElements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalElements")
  public Long getTotalElements() {
    return totalElements;
  }

  public void setTotalElements(Long totalElements) {
    this.totalElements = totalElements;
  }

  public ComplaintListVTO totalPages(Integer totalPages) {
    this.totalPages = totalPages;
    return this;
  }

  /**
   * Get totalPages
   * @return totalPages
  */
  
  @Schema(name = "totalPages", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalPages")
  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }

  public ComplaintListVTO currentPage(Integer currentPage) {
    this.currentPage = currentPage;
    return this;
  }

  /**
   * Get currentPage
   * @return currentPage
  */
  
  @Schema(name = "currentPage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currentPage")
  public Integer getCurrentPage() {
    return currentPage;
  }

  public void setCurrentPage(Integer currentPage) {
    this.currentPage = currentPage;
  }

  public ComplaintListVTO pageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  /**
   * Get pageSize
   * @return pageSize
  */
  
  @Schema(name = "pageSize", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pageSize")
  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public ComplaintListVTO hasNext(Boolean hasNext) {
    this.hasNext = hasNext;
    return this;
  }

  /**
   * Get hasNext
   * @return hasNext
  */
  
  @Schema(name = "hasNext", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hasNext")
  public Boolean getHasNext() {
    return hasNext;
  }

  public void setHasNext(Boolean hasNext) {
    this.hasNext = hasNext;
  }

  public ComplaintListVTO hasPrevious(Boolean hasPrevious) {
    this.hasPrevious = hasPrevious;
    return this;
  }

  /**
   * Get hasPrevious
   * @return hasPrevious
  */
  
  @Schema(name = "hasPrevious", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hasPrevious")
  public Boolean getHasPrevious() {
    return hasPrevious;
  }

  public void setHasPrevious(Boolean hasPrevious) {
    this.hasPrevious = hasPrevious;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ComplaintListVTO complaintListVTO = (ComplaintListVTO) o;
    return Objects.equals(this.complaints, complaintListVTO.complaints) &&
        Objects.equals(this.totalElements, complaintListVTO.totalElements) &&
        Objects.equals(this.totalPages, complaintListVTO.totalPages) &&
        Objects.equals(this.currentPage, complaintListVTO.currentPage) &&
        Objects.equals(this.pageSize, complaintListVTO.pageSize) &&
        Objects.equals(this.hasNext, complaintListVTO.hasNext) &&
        Objects.equals(this.hasPrevious, complaintListVTO.hasPrevious);
  }

  @Override
  public int hashCode() {
    return Objects.hash(complaints, totalElements, totalPages, currentPage, pageSize, hasNext, hasPrevious);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ComplaintListVTO {\n");
    sb.append("    complaints: ").append(toIndentedString(complaints)).append("\n");
    sb.append("    totalElements: ").append(toIndentedString(totalElements)).append("\n");
    sb.append("    totalPages: ").append(toIndentedString(totalPages)).append("\n");
    sb.append("    currentPage: ").append(toIndentedString(currentPage)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    hasNext: ").append(toIndentedString(hasNext)).append("\n");
    sb.append("    hasPrevious: ").append(toIndentedString(hasPrevious)).append("\n");
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

