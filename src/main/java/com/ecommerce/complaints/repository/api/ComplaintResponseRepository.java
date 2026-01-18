package com.ecommerce.complaints.repository.api;

import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.model.entity.ComplaintResponse;
import com.ecommerce.complaints.model.enums.ResponseStatus;

import java.util.List;
import java.util.Optional;

public interface ComplaintResponseRepository {
    ComplaintResponse save (ComplaintResponse  complaintResponse);
    Optional<ComplaintResponse> findById(Long id);
    boolean existsByComplaintId(Long complaintId);
    List<ComplaintResponse> findPendingApprovals();
    long countByStatus(ResponseStatus status);
    long count ();
    List<ComplaintResponse> findAll();
}
