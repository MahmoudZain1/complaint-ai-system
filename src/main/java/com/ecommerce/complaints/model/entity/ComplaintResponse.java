package com.ecommerce.complaints.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Complaint complaint;

    @Column(columnDefinition = "TEXT")
    private String generatedResponse;

    private String tone;

    private Double confidenceScore;

    private LocalDateTime generatedAt;
}
