package com.ecommerce.complaints.repository.jpa;

import com.ecommerce.complaints.model.entity.ComplaintResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ComplaintResponseJpaRepository  extends JpaRepository<ComplaintResponse, Long> {

    @Query("""
            SELECT CASE WHEN COUNT(cr) > 0 THEN true ELSE false END  
            FROM ComplaintResponse cr WHERE cr.complaint.id = :complaintId
            """)
    boolean existsByComplaintId(@Param("complaintId") Long complaintId);
}
