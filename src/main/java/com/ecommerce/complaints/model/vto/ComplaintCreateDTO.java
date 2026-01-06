package com.ecommerce.complaints.model.vto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ComplaintCreateDTO
 */
@lombok.experimental.SuperBuilder(toBuilder = true)
@lombok.Data
@lombok.extern.jackson.Jacksonized

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class ComplaintCreateDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private String customerId;

  private String customerName;

  private String customerEmail;

  private String subject;

  private String description;

  public ComplaintCreateDTO() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ComplaintCreateDTO(String customerId, String customerName, String customerEmail, String subject, String description) {
    this.customerId = customerId;
    this.customerName = customerName;
    this.customerEmail = customerEmail;
    this.subject = subject;
    this.description = description;
  }

  public ComplaintCreateDTO customerId(String customerId) {
    this.customerId = customerId;
    return this;
  }

  /**
   * Get customerId
   * @return customerId
  */
  @NotNull @Size(min = 1, max = 100) 
  @Schema(name = "customerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("customerId")
  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public ComplaintCreateDTO customerName(String customerName) {
    this.customerName = customerName;
    return this;
  }

  /**
   * Get customerName
   * @return customerName
  */
  @NotNull @Size(min = 2, max = 100) 
  @Schema(name = "customerName", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("customerName")
  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public ComplaintCreateDTO customerEmail(String customerEmail) {
    this.customerEmail = customerEmail;
    return this;
  }

  /**
   * Get customerEmail
   * @return customerEmail
  */
  @NotNull @Size(max = 255) @jakarta.validation.constraints.Email
  @Schema(name = "customerEmail", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("customerEmail")
  public String getCustomerEmail() {
    return customerEmail;
  }

  public void setCustomerEmail(String customerEmail) {
    this.customerEmail = customerEmail;
  }

  public ComplaintCreateDTO subject(String subject) {
    this.subject = subject;
    return this;
  }

  /**
   * Get subject
   * @return subject
  */
  @NotNull @Size(min = 5, max = 255) 
  @Schema(name = "subject", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("subject")
  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public ComplaintCreateDTO description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
  */
  @NotNull @Size(min = 10, max = 5000) 
  @Schema(name = "description", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ComplaintCreateDTO complaintCreateDTO = (ComplaintCreateDTO) o;
    return Objects.equals(this.customerId, complaintCreateDTO.customerId) &&
        Objects.equals(this.customerName, complaintCreateDTO.customerName) &&
        Objects.equals(this.customerEmail, complaintCreateDTO.customerEmail) &&
        Objects.equals(this.subject, complaintCreateDTO.subject) &&
        Objects.equals(this.description, complaintCreateDTO.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerId, customerName, customerEmail, subject, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ComplaintCreateDTO {\n");
    sb.append("    customerId: ").append(toIndentedString(customerId)).append("\n");
    sb.append("    customerName: ").append(toIndentedString(customerName)).append("\n");
    sb.append("    customerEmail: ").append(toIndentedString(customerEmail)).append("\n");
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

