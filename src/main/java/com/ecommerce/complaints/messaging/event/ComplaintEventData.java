package com.ecommerce.complaints.messaging.event;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;

@Value
@Builder
public class ComplaintEventData implements Serializable {
    Long complaintId;
    String customerId;
    String customerName;
    String customerEmail;
    String subject;
    String description;
    LocalDateTime createdAt;
}
