package com.ecommerce.complaints.model.error;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseErrors {

    INVALID_ACTION("INVALID_ACTION", "Invalid action "),
    EMPTY_EDITED_RESPONSE("EMPTY_EDITED_RESPONSE", "Edited response cannot be empty"),
    REJECTION_REASON_REQUIRED("REJECTION_REASON_REQUIRED", "Rejection reason is required"),
    INVALID_RESPONSE_STATUS("INVALID_RESPONSE_STATUS", "Response is not in PENDING_APPROVAL status");


    private final String code;
    private final String message;

}
