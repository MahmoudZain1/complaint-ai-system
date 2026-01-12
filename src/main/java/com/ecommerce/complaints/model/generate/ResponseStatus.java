package com.ecommerce.complaints.model.generate;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets ResponseStatus
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
public enum ResponseStatus {
  
  PENDING_APPROVAL("PENDING_APPROVAL"),
  
  APPROVED("APPROVED"),
  
  REJECTED("REJECTED"),
  
  EDITED("EDITED"),
  
  SENT("SENT");

  private String value;

  ResponseStatus(String value) {
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
  public static ResponseStatus fromValue(String value) {
    for (ResponseStatus b : ResponseStatus.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

