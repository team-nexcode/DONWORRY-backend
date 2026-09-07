package com.donworry.backend.repository;

import com.donworry.backend.entity.TransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferHistoryRepository extends JpaRepository<TransferHistory, Long> {
    boolean existsByUserIdAndRecipientAccount(String userId, String recipientAccount);
}
