package com.donworry.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "account_limit_logs")
@Getter
@NoArgsConstructor
public class AccountLimitLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private LocalDateTime changedAt;
    private boolean isIncreased;

    public AccountLimitLog(String userId, LocalDateTime changedAt, boolean isIncreased) {
        this.userId = userId;
        this.changedAt = changedAt;
        this.isIncreased = isIncreased;
    }
}
