package com.ecommerce.complaints.controller;

import com.ecommerce.complaints.controller.api.AiAnalysisApi;
import com.ecommerce.complaints.model.generate.*;
import com.ecommerce.complaints.service.api.ComplaintResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AiAnalysisApiImpl implements AiAnalysisApi {

    private final ComplaintResponseService complaintResponseService;
    @Override
    public ResponseEntity<ComplaintAnalysisVTO> analyzeComplaint(Long id) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @Override
    public ResponseEntity<ComplaintResponseVTO> generateResponse(Long id, ResponseGenerationRequestDTO responseGenerationRequestDTO) {
        return ResponseEntity.ok().body(complaintResponseService.generateResponse(id));
    }
}
