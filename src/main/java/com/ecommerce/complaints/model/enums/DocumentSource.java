package com.ecommerce.complaints.model.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DocumentSource {

    POLICY("POLICY", "Company Policy"),
    COMPLAINT("COMPLAINT", "Customer Complaint"),
    RESPONSE("RESPONSE", "Generated Response");

    private final String code;
    private final String displayName;


    public String getFilterExpression() {
        return String.format("source == '%s'", code);
    }
}