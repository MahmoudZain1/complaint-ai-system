package com.ecommerce.complaints.controller;

import com.ecommerce.complaints.controller.api.ComplaintsApi;
import com.ecommerce.complaints.model.enums.ComplaintCategory;
import com.ecommerce.complaints.model.enums.ComplaintStatus;
import com.ecommerce.complaints.model.enums.Priority;
import com.ecommerce.complaints.model.generate.*;
import com.ecommerce.complaints.service.api.ComplaintService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class ComplaintsApiImpl implements ComplaintsApi {


    private final ComplaintService complaintService;

    @Override
    public ResponseEntity<ComplaintVTO> createComplaint(ComplaintCreateDTO dto) {
        ComplaintVTO created = complaintService.createComplaint(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<Void> deleteComplaint(Long id) {
        complaintService.deleteComplaint(id);
        return ResponseEntity.noContent().build();
    }


    @Override
    public ResponseEntity<ComplaintVTO> updateComplaint(Long id, ComplaintUpdateDTO dto) {
        ComplaintVTO updated = complaintService.updateComplaint(id, dto);
        return ResponseEntity.ok(updated);
    }
    @Override
    public ResponseEntity<ComplaintVTO> getComplaintById(Long id) {
        return ResponseEntity.ok().body(complaintService.getComplaintById(id));
    }

    @Override
    public ResponseEntity<ComplaintListVTO> listComplaints(ComplaintStatus status, ComplaintCategory category, Priority priority, Long page, Long size, String sortBy, String sortDirection) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }


    @Override
    public ResponseEntity<ComplaintVTO> updateComplaintStatus(Long id, UpdateComplaintStatusRequest updateComplaintStatusRequest) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
