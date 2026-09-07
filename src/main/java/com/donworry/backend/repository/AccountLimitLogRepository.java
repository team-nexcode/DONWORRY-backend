package com.donworry.backend.repository;

import com.donworry.backend.entity.AccountLimitLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AccountLimitLogRepository extends JpaRepository<AccountLimitLog, Long> {
    boolean existsByUserIdAndChangedAtAfter(String userId, LocalDateTime dataTime);
}
