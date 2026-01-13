package com.ecommerce.complaints.controller;

import com.ecommerce.complaints.controller.api.ResponseApprovalApi;
import com.ecommerce.complaints.model.generate.GetPendingCount200Response;
import com.ecommerce.complaints.model.generate.ResponseApprovalDTO;
import com.ecommerce.complaints.model.generate.ResponseReviewVTO;
import com.ecommerce.complaints.service.ResponseApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ResponseApprovalApiImpl implements ResponseApprovalApi {

    private final ResponseApprovalService approvalService;

    @Override
    public ResponseEntity<List<ResponseReviewVTO>> getPendingApprovals() {
        List<ResponseReviewVTO> pending = approvalService.getPendingApprovals();
        return ResponseEntity.ok(pending);
    }

    @Override
    public ResponseEntity<GetPendingCount200Response> getPendingCount() {
        long count = approvalService.getPendingApprovalsCount();
        GetPendingCount200Response response = GetPendingCount200Response.builder()
                .pendingCount(count).build();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ResponseReviewVTO> reviewResponse(ResponseApprovalDTO responseApprovalDTO) {
        ResponseReviewVTO reviewed = approvalService.reviewResponse(responseApprovalDTO);
        return ResponseEntity.ok(reviewed);
    }
}
