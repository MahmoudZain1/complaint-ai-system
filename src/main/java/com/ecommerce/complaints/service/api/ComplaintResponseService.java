package com.ecommerce.complaints.service.api;

import com.ecommerce.complaints.model.generate.ComplaintResponseVTO;

public interface ComplaintResponseService {
    ComplaintResponseVTO generateResponse(Long complaintId);

}
