package com.ecommerce.complaints.model.generate;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetPendingCount200Response
 */
@lombok.experimental.SuperBuilder(toBuilder = true)
@lombok.Data
@lombok.extern.jackson.Jacksonized

@JsonTypeName("getPendingCount_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class GetPendingCount200Response implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long pendingCount;

  public GetPendingCount200Response pendingCount(Long pendingCount) {
    this.pendingCount = pendingCount;
    return this;
  }

  /**
   * Get pendingCount
   * @return pendingCount
  */
  
  @Schema(name = "pendingCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pendingCount")
  public Long getPendingCount() {
    return pendingCount;
  }

  public void setPendingCount(Long pendingCount) {
    this.pendingCount = pendingCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetPendingCount200Response getPendingCount200Response = (GetPendingCount200Response) o;
    return Objects.equals(this.pendingCount, getPendingCount200Response.pendingCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pendingCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetPendingCount200Response {\n");
    sb.append("    pendingCount: ").append(toIndentedString(pendingCount)).append("\n");
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

