package com.donworry.backend.dto;

import java.util.List;

public record RiskAnalysisResponse(
        String transferId,
        String status,
        List<String> riskSignals,
        List<String> explainMessages
) {}
