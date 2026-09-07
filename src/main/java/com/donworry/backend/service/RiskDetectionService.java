package com.donworry.backend.service;

import com.donworry.backend.dto.AiAnalysisRequest;
import com.donworry.backend.dto.AiAnalysisResponse;
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
    private final AiClientService aiClientService;

    public RiskAnalysisResponse analyzeRisk(TransferRequest request) {
        List<String> signals = new ArrayList<>();
        List<String> explains = new ArrayList<>();

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다: " + request.userId()));

        // 1. 고액 송금 탐지
        if (request.amount() > user.getAvgTransferAmount() * 3) {
            signals.add("LARGE_AMOUNT");
            explains.add("평균 송금액(" + String.format("%,d", user.getAvgTransferAmount()) + "원) 대비 고액 송금 시도입니다.");
        }

        // 2. 신규 수취 계좌 탐지
        boolean hasHistory = transferHistoryRepository.existsByUserIdAndRecipientAccount(request.userId(), request.recipientAccount());
        if (!hasHistory) {
            signals.add("NEW_RECIPIENT");
            explains.add("신규 수취인 계좌입니다.");
        }

        // 3. 최근 24시간 한도 변경 이력 탐지
        LocalDateTime last24Hours = LocalDateTime.now().minusHours(24);
        boolean hasLimitChangedRecently = limitLogRepository.existsByUserIdAndChangedAtAfter(request.userId(), last24Hours);
        if (hasLimitChangedRecently) {
            signals.add("LIMIT_CHANGED");
            explains.add("최근 24시간 이내에 이체 한도가 변경된 계좌입니다.");
        }

        String status = signals.isEmpty() ? "ALLOW" : "PAUSE";
        String transferId = "TRX_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 4. AI 서버 요청 데이터 조립 (AI 규격 매핑)
        String riskLevel = signals.size() >= 2 ? "HIGH" : (signals.size() == 1 ? "MEDIUM" : "LOW");

        AiAnalysisRequest.TransactionContext context = new AiAnalysisRequest.TransactionContext(
                request.amount(),
                user.getAvgTransferAmount(),
                request.recipientName()
        );

        // userStatement: TransferRequest에 필드가 없으면 기본 테스트 문구 사용
        String userStatement = "검찰청 검사님이 안전계좌로 피해보원금을 보관해야 한다고 했어요.";

        AiAnalysisRequest aiRequest = new AiAnalysisRequest(
                transferId,
                request.userId(),
                riskLevel,
                signals,
                context,
                userStatement
        );

        // 5. AI 서비스 연동
        AiAnalysisResponse aiResponse = aiClientService.analyzeWithAi(aiRequest);

        return new RiskAnalysisResponse(transferId, status, signals, explains, aiResponse);
    }
}