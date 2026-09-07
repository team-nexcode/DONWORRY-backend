package com.donworry.backend.controller;

import com.donworry.backend.dto.RiskAnalysisResponse;
import com.donworry.backend.dto.TransferRequest;
import com.donworry.backend.service.RiskDetectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transfer")
@RequiredArgsConstructor
public class TransferController {
    private final RiskDetectionService riskDetectionService;

    @PostMapping("/analyze")
    public ResponseEntity<RiskAnalysisResponse> analyzeTransfer(@RequestBody TransferRequest request) {
        RiskAnalysisResponse response = riskDetectionService.analyzeRisk(request);

        return ResponseEntity.ok(response);
    }
}
