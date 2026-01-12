package com.ecommerce.complaints.service.api;

import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.model.generate.ComplaintAnalysisVTO;
import com.ecommerce.complaints.model.generate.ComplaintResponseVTO;

import java.io.IOException;

public interface AIAnalysisService {

    ComplaintAnalysisVTO analyzeComplaint(Complaint complaint) throws IOException;
    void processAiAnalysis(Long complaintId, String content) throws IOException;
    ComplaintResponseVTO generateResponse(Long complaintId) ;

}
