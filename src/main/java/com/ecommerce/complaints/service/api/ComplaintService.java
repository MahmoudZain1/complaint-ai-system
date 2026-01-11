package com.ecommerce.complaints.service.api;

import com.ecommerce.complaints.model.enums.ComplaintCategory;
import com.ecommerce.complaints.model.enums.ComplaintStatus;
import com.ecommerce.complaints.model.enums.Priority;
import com.ecommerce.complaints.model.enums.Sentiment;
import com.ecommerce.complaints.model.generate.*;

public interface ComplaintService {

    ComplaintVTO createComplaint(ComplaintCreateDTO complaintCreateDTO);
    ComplaintVTO getComplaintById(Long id);
    ComplaintVTO updateComplaint(Long id, ComplaintUpdateDTO complaintUpdateDTO);
    void deleteComplaint(Long id);
    ComplaintListVTO listComplaints(ComplaintStatus status, ComplaintCategory category, Priority priority,
                                    Sentiment sentiment, Integer page, Integer size, String sortBy,
                                    String sortDirection);
}
