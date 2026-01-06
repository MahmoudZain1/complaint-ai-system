package com.ecommerce.complaints.model.entity;

import com.ecommerce.complaints.model.enums.ComplaintCategory;
import com.ecommerce.complaints.model.enums.ComplaintStatus;
import com.ecommerce.complaints.model.enums.Priority;
import com.ecommerce.complaints.model.enums.Sentiment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "complaints")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Complaint  implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "subject")
    private String subject;

    @Column(name = "description" , columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private ComplaintCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ComplaintStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "sentiment")
    private Sentiment sentiment;

    @Column(name = "ai_analysis" ,  columnDefinition = "TEXT")
    private String aiAnalysis;

    @OneToOne(mappedBy = "complaint")
    private ComplaintResponse response;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
