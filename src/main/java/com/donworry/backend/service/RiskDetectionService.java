package com.donworry.backend.service;

import com.donworry.backend.dto.RiskAnalysisResponse;
import com.donworry.backend.dto.TransferRequest;
import com.donworry.backend.entity.User;
import com.donworry.backend.repository.AccountLimitLogRepository;
import com.donworry.backend.repository.TransferHistoryRepository;
import com.donworry.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RiskDetectionService {
    private final UserRepository userRepository;
    private final TransferHistoryRepository transferHistoryRepository;
    private final AccountLimitLogRepository limitLogRepository;

    public RiskAnalysisResponse analyzeRisk(TransferRequest request) {
        List<String> signals = new ArrayList<>();
        List<String> explains = new ArrayList<>();

        User user = userRepository.findById(request.userId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + request.userId()));

        if(request.amount() > user.getAvgTransferAmount() * 3) {
            signals.add("LARGE_AMOUNT");
            explains.add("평균 송금액(" + String.format("%,d", user.getAvgTransferAmount()) + "원) 대비 고액 송금 시도입니다.");
        }

        boolean hasHistory = transferHistoryRepository.existsByUserIdAndRecipientAccount(request.userId(), request.recipientAccount());
        if(!hasHistory) {
            signals.add("NEW_RECIPIENT");
            explains.add("식규 수취인 계좌입니다.");
        }

        LocalDateTime last24Hours = LocalDateTime.now().minusHours(24);
        boolean hasLimitChangedRecently = limitLogRepository.existsByUserIdAndChangedAtAfter(request.userId(), last24Hours);
        if (hasLimitChangedRecently) {
            signals.add("LIMIT_CHANGED_RECENTLY");
            explains.add("최근 24시간 이내에 이체 한도가 변경된 계좌입니다.");
        }

        String status = signals.isEmpty() ? "ALLOW" : "PAUSE";
        String transferId = "TRX_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        return new RiskAnalysisResponse(transferId, status, signals, explains);
    }
}
