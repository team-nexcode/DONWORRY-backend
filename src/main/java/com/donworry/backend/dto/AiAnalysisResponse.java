package com.donworry.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record AiAnalysisResponse(
        @JsonProperty("analysisStatus")
        String analysisStatus,

        @JsonProperty("phishingType")
        String phishingType,

        @JsonProperty("explanation")
        String explanation,

        @JsonProperty("detectedContexts")
        List<String> detectedContexts,

        @JsonProperty("actionGuidance")
        List<ActionGuidance> actionGuidance,

        @JsonProperty("sources")
        List<Source> sources,

        @JsonProperty("transactionId")
        String transactionId,

        @JsonProperty("errorCode")
        String errorCode
) {
    public record ActionGuidance(
            String code,
            String label,
            String description
    ) {}

    public record Source(
            String title,
            String publisher,
            String publishedAt,
            String url
    ) {}

    public static AiAnalysisResponse fallback() {
        return new AiAnalysisResponse(
                "FALLBACK",
                "UNKNOWN",
                "AI 서버 응답 지연",
                List.of(),
                List.of(new ActionGuidance("CAUTION", "주의 필요", "신중히 확인 후 송금하세요.")),
                List.of(),
                null,
                "TIMEOUT_OR_ERROR"
        );
    }
}