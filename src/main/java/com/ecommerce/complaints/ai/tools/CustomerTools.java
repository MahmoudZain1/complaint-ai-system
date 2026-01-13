package com.ecommerce.complaints.ai.tools;

import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.model.entity.User;
import com.ecommerce.complaints.repository.api.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerTools {


    private final ComplaintRepository complaintRepository;


    @Tool(name = "getCustomerName", description = "Get customer name from complaint by ID")
    public String getCustomerName(@ToolParam(description = "Complaint ID") Long complaintId) {
        Optional<Complaint> complaint = complaintRepository.findById(complaintId);
        User customer = complaint.get().getCustomer();
        if (customer != null && customer.getName() != null) {
            return customer.getName();
        }return "Dear ";
    }

}
