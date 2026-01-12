package com.ecommerce.complaints.repository.jpa;

import com.ecommerce.complaints.model.entity.ComplaintResponse;
import com.ecommerce.complaints.model.enums.ResponseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComplaintResponseJpaRepository  extends JpaRepository<ComplaintResponse, Long> {

    @Query("""
            SELECT CASE WHEN COUNT(cr) > 0 THEN true ELSE false END  
            FROM ComplaintResponse cr WHERE cr.complaint.id = :complaintId
            """)
    boolean existsByComplaintId(@Param("complaintId") Long complaintId);

    long countByStatus(ResponseStatus status);

    @Query("SELECT cr FROM ComplaintResponse cr " +
            "JOIN FETCH cr.complaint c " +
            "WHERE cr.status = :status " +
            "ORDER BY cr.generatedAt ASC"
    )
    List<ComplaintResponse> findPendingWithComplaint(ResponseStatus status);
}
