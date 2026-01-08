package com.ecommerce.complaints.model.vto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UpdateComplaintStatusRequest
 */
@lombok.experimental.SuperBuilder(toBuilder = true)
@lombok.Data
@lombok.extern.jackson.Jacksonized

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class UpdateComplaintStatusRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private com.ecommerce.complaints.model.enums.ComplaintStatus status;

  private String note;

  public UpdateComplaintStatusRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public UpdateComplaintStatusRequest(com.ecommerce.complaints.model.enums.ComplaintStatus status) {
    this.status = status;
  }

  public UpdateComplaintStatusRequest status(com.ecommerce.complaints.model.enums.ComplaintStatus status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  */
  @NotNull @Valid 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public com.ecommerce.complaints.model.enums.ComplaintStatus getStatus() {
    return status;
  }

  public void setStatus(com.ecommerce.complaints.model.enums.ComplaintStatus status) {
    this.status = status;
  }

  public UpdateComplaintStatusRequest note(String note) {
    this.note = note;
    return this;
  }

  /**
   * Get note
   * @return note
  */
  
  @Schema(name = "note", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("note")
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateComplaintStatusRequest updateComplaintStatusRequest = (UpdateComplaintStatusRequest) o;
    return Objects.equals(this.status, updateComplaintStatusRequest.status) &&
        Objects.equals(this.note, updateComplaintStatusRequest.note);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, note);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateComplaintStatusRequest {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
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

