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
 * ResponseGenerationRequestDTO
 */
@lombok.experimental.SuperBuilder(toBuilder = true)
@lombok.Data
@lombok.extern.jackson.Jacksonized

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public class ResponseGenerationRequestDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * Gets or Sets tone
   */
  public enum ToneEnum {
    PROFESSIONAL("professional"),
    
    EMPATHETIC("empathetic"),
    
    APOLOGETIC("apologetic"),
    
    FRIENDLY("friendly");

    private String value;

    ToneEnum(String value) {
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
    public static ToneEnum fromValue(String value) {
      for (ToneEnum b : ToneEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ToneEnum tone = ToneEnum.PROFESSIONAL;

  private Boolean includeCompensation = false;

  private String customInstructions;

  public ResponseGenerationRequestDTO tone(ToneEnum tone) {
    this.tone = tone;
    return this;
  }

  /**
   * Get tone
   * @return tone
  */
  
  @Schema(name = "tone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tone")
  public ToneEnum getTone() {
    return tone;
  }

  public void setTone(ToneEnum tone) {
    this.tone = tone;
  }

  public ResponseGenerationRequestDTO includeCompensation(Boolean includeCompensation) {
    this.includeCompensation = includeCompensation;
    return this;
  }

  /**
   * Get includeCompensation
   * @return includeCompensation
  */
  
  @Schema(name = "includeCompensation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("includeCompensation")
  public Boolean getIncludeCompensation() {
    return includeCompensation;
  }

  public void setIncludeCompensation(Boolean includeCompensation) {
    this.includeCompensation = includeCompensation;
  }

  public ResponseGenerationRequestDTO customInstructions(String customInstructions) {
    this.customInstructions = customInstructions;
    return this;
  }

  /**
   * Get customInstructions
   * @return customInstructions
  */
  
  @Schema(name = "customInstructions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("customInstructions")
  public String getCustomInstructions() {
    return customInstructions;
  }

  public void setCustomInstructions(String customInstructions) {
    this.customInstructions = customInstructions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseGenerationRequestDTO responseGenerationRequestDTO = (ResponseGenerationRequestDTO) o;
    return Objects.equals(this.tone, responseGenerationRequestDTO.tone) &&
        Objects.equals(this.includeCompensation, responseGenerationRequestDTO.includeCompensation) &&
        Objects.equals(this.customInstructions, responseGenerationRequestDTO.customInstructions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tone, includeCompensation, customInstructions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResponseGenerationRequestDTO {\n");
    sb.append("    tone: ").append(toIndentedString(tone)).append("\n");
    sb.append("    includeCompensation: ").append(toIndentedString(includeCompensation)).append("\n");
    sb.append("    customInstructions: ").append(toIndentedString(customInstructions)).append("\n");
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

