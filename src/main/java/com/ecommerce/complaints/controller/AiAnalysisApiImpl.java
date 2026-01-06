package com.ecommerce.complaints.controller;

import com.ecommerce.complaints.controller.api.AiAnalysisApi;
import com.ecommerce.complaints.model.vto.*;
import com.ecommerce.complaints.service.api.AIAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AiAnalysisApiImpl implements AiAnalysisApi {

    private final AIAnalysisService  aiAnalysisService;

    @Override
    public ResponseEntity<ComplaintAnalysisVTO> analyzeComplaint(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<ComplaintResponseVTO> generateResponse(Long id, ResponseGenerationRequestDTO responseGenerationRequestDTO) throws IOException {
        return ResponseEntity.ok().body( aiAnalysisService.generateResponse(id));
    }
}
