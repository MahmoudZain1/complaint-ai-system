package com.ecommerce.complaints.service.api;

import com.ecommerce.complaints.model.dto.ComplaintSearchRequest;
import com.ecommerce.complaints.model.generate.*;

public interface ComplaintService {

    ComplaintVTO createComplaint(ComplaintCreateDTO complaintCreateDTO);
    ComplaintVTO getComplaintById(Long id);
    ComplaintVTO updateComplaint(Long id, ComplaintUpdateDTO complaintUpdateDTO);
    void deleteComplaint(Long id);
    ComplaintListVTO listComplaints(ComplaintSearchRequest request);

}
