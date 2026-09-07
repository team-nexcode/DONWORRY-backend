package com.donworry.backend.dto;

public record TransferRequest(
        String userId,
        Long amount,
        String recipientAccount,
        String recipientName
) {}