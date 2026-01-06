package com.ecommerce.complaints.messaging.event;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class ComplaintAiAnalysiEvent  implements Serializable {
    Long complaintId;
    String content;
}
