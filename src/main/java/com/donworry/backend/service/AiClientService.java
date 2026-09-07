package com.donworry.backend.service;

import com.donworry.backend.dto.AiAnalysisRequest;
import com.donworry.backend.dto.AiAnalysisResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiClientService {
    private final WebClient aiWebClient;

    public AiAnalysisResponse analyzeWithAi(AiAnalysisRequest request) {
        return aiWebClient.post()
                .uri("/api/v1/ai/analyze")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AiAnalysisResponse.class)
                .timeout(Duration.ofSeconds(10))
                .onErrorResume(throwable -> {
                    log.error("AI 오류 발생 (Fallback): {}", throwable.getMessage());
                    return Mono.just(AiAnalysisResponse.fallback());
                })
                .block();
    }
}
