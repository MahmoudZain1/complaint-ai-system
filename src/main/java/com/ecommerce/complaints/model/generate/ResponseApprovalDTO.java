package com.ecommerce.complaints.model.generate;

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
 * ResponseApprovalDTO
 */
@lombok.experimental.SuperBuilder(toBuilder = true)
@lombok.Data
@lombok.extern.jackson.Jacksonized

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class ResponseApprovalDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long responseId;

  /**
   * Gets or Sets action
   */
  public enum ActionEnum {
    APPROVE("APPROVE"),
    
    REJECT("REJECT"),
    
    EDIT("EDIT");

    private String value;

    ActionEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static ActionEnum fromValue(String value) {
      for (ActionEnum b : ActionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ActionEnum action;

  private String rejectionReason;

  private String editedResponse;

  public ResponseApprovalDTO() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ResponseApprovalDTO(Long responseId, ActionEnum action) {
    this.responseId = responseId;
    this.action = action;
  }

  public ResponseApprovalDTO responseId(Long responseId) {
    this.responseId = responseId;
    return this;
  }

  /**
   * Get responseId
   * @return responseId
  */
  @NotNull 
  @Schema(name = "responseId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("responseId")
  public Long getResponseId() {
    return responseId;
  }

  public void setResponseId(Long responseId) {
    this.responseId = responseId;
  }

  public ResponseApprovalDTO action(ActionEnum action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
  */
  @NotNull 
  @Schema(name = "action", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("action")
  public ActionEnum getAction() {
    return action;
  }

  public void setAction(ActionEnum action) {
    this.action = action;
  }

  public ResponseApprovalDTO rejectionReason(String rejectionReason) {
    this.rejectionReason = rejectionReason;
    return this;
  }

  /**
   * Get rejectionReason
   * @return rejectionReason
  */
  
  @Schema(name = "rejectionReason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rejectionReason")
  public String getRejectionReason() {
    return rejectionReason;
  }

  public void setRejectionReason(String rejectionReason) {
    this.rejectionReason = rejectionReason;
  }

  public ResponseApprovalDTO editedResponse(String editedResponse) {
    this.editedResponse = editedResponse;
    return this;
  }

  /**
   * Get editedResponse
   * @return editedResponse
  */
  
  @Schema(name = "editedResponse", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("editedResponse")
  public String getEditedResponse() {
    return editedResponse;
  }

  public void setEditedResponse(String editedResponse) {
    this.editedResponse = editedResponse;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseApprovalDTO responseApprovalDTO = (ResponseApprovalDTO) o;
    return Objects.equals(this.responseId, responseApprovalDTO.responseId) &&
        Objects.equals(this.action, responseApprovalDTO.action) &&
        Objects.equals(this.rejectionReason, responseApprovalDTO.rejectionReason) &&
        Objects.equals(this.editedResponse, responseApprovalDTO.editedResponse);
  }

  @Override
  public int hashCode() {
    return Objects.hash(responseId, action, rejectionReason, editedResponse);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResponseApprovalDTO {\n");
    sb.append("    responseId: ").append(toIndentedString(responseId)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    rejectionReason: ").append(toIndentedString(rejectionReason)).append("\n");
    sb.append("    editedResponse: ").append(toIndentedString(editedResponse)).append("\n");
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

