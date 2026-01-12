package com.ecommerce.complaints.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseStatus {

    PENDING_APPROVAL("PENDING_APPROVAL", "Waiting for employee review"),
    APPROVED("APPROVED", "Approved by employee"),
    REJECTED("REJECTED", "Rejected by employee"),
    EDITED("EDITED", "Edited by employee"),
    SENT("SENT", "Sent to customer");

    private final String code;
    private final String description;
}
