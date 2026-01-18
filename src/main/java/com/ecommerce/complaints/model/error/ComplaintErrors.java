package com.ecommerce.complaints.model.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ComplaintErrors {

    COMPLAINT_NOT_FOUND("COMPLAINT_NOT_FOUND", "Complaint not found with ID: {0}"),
    INVALID_INPUT("INVALID_INPUT", "{0}"),
    MISSING_REQUIRED_FIELD("MISSING_REQUIRED_FIELD", "{0}"),
    VALIDATION_ERROR("VALIDATION_ERROR", "{0}"),
    DUPLICATE_COMPLAINT("DUPLICATE_COMPLAINT", "Duplicate complaint detected"),
    RESPONSE_ALREADY_EXISTS("RESPONSE_ALREADY_EXISTS", "Response already exists"),
    AI_ANALYSIS_FAILED("AI_ANALYSIS_FAILED" , "AI analysis failed for complaint");

    private final String code;
    private final String message;


}
