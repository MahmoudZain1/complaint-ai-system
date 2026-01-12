package com.ecommerce.complaints.model.entity;

import com.ecommerce.complaints.model.enums.ResponseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "complaint_responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "complaint_id")
    @ToString.Exclude
    private Complaint complaint;

    @Column(columnDefinition = "TEXT")
    private String generatedResponse;

    @Column(columnDefinition = "TEXT")
    private String editedResponse;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    private String tone;

    private Double confidenceScore;

    private LocalDateTime generatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ResponseStatus status = ResponseStatus.PENDING_APPROVAL;

    @ManyToOne
    @JoinColumn(name = "reviewed_by")
    @ToString.Exclude
    private User reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "auto_approved")
    private boolean autoApproved = false;
}
