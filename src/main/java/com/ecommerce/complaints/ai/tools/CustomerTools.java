package com.ecommerce.complaints.ai.tools;

import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.repoistory.api.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerTools {


    private final ComplaintRepository complaintRepository;
    private final VectorStore vectorStore;


    @Tool(name = "getCustomerName" , description = "Get customer name from complaint by ID")
    public String getCustomerName(@ToolParam(description = "Complaint ID") Long complaintId) {
        Optional<Complaint> complaint = complaintRepository.findById(complaintId);
        return complaint.map(Complaint::getCustomerName).orElse("Dear");
    }

}
