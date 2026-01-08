package com.ecommerce.complaints.repository.api;

import com.ecommerce.complaints.model.entity.ComplaintResponse;

public interface ComplaintResponseRepository {
    ComplaintResponse save (ComplaintResponse  complaintResponse);
    boolean existsByComplaintId(Long complaintId);
}
