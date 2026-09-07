package com.donworry.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${ai.server.base-url}")
    private String aiBaseUrl;

    @Value("${ai.server.token}")
    private String aiServiceToken;

    @Bean
    public WebClient aiWebClient() {
        return WebClient.builder().baseUrl(aiBaseUrl).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).defaultHeader("X-AI-Service-Token", aiServiceToken).build();
    }
}
