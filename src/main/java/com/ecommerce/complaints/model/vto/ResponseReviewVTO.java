package com.ecommerce.complaints.model.vto;

import com.ecommerce.complaints.model.enums.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseReviewVTO {

    private Long id;
    private Long complaintId;
    private String complaintSubject;
    private String complaintDescription;
    private String customerName;
    private String customerEmail;
    private String generatedResponse;
    private String tone;
    private Double confidenceScore;
    private LocalDateTime generatedAt;
    private ResponseStatus status;
    private String priority;
    private String sentiment;
    private String category;
}
