package com.donworry.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Table(name = "transfer_histories")
@Getter
@NoArgsConstructor
public class TransferHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String recipientAccount;
    private String recipientName;
    private Long amount;
    private LocalDateTime createdAt;

    public TransferHistory(String userId, String recipientAccount, String recipientName, Long amount) {
        this.userId = userId;
        this.recipientAccount = recipientAccount;
        this.recipientName = recipientName;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
    }
}
