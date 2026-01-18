package com.ecommerce.complaints.ai.tools;

import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.model.entity.User;
import com.ecommerce.complaints.repository.api.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerTools {


    private final ComplaintRepository complaintRepository;


    @Tool(name = "getCustomerName", description = "Get customer name from complaint by ID")
    public String getCustomerName(@ToolParam(description = "Complaint ID") Long complaintId) {
        return complaintRepository.findById(complaintId)
                .map(Complaint::getCustomer)
                .map(User::getName)
                .orElse("Dear Customer");
    }

}
