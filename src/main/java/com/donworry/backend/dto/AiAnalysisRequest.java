package com.donworry.backend.dto;

import java.util.List;

public record AiAnalysisRequest(
        String transactionId,
        String userId,
        String riskLevel,
        List<String> riskSignals,
        TransactionContext transactionContext,
        String userStatement
) {
    public record TransactionContext(
            Long amount,
            Long avgAmount,
            String recipientName
    ) {}
}