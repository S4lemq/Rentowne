package pl.rentowne.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rentowne.security.repository.TokenRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenCleanupService {

    private final TokenRepository tokenRepository;

    @Transactional
    @Scheduled(cron = "${application.token.cleanup.expression}")
    public void cleanupExpiredAndRevokedTokens() {
        List<Long> tokenIds = tokenRepository.findAllExpiredAndRevokedJwtToken();
        if (!tokenIds.isEmpty()) {
            tokenRepository.deleteAllByIds(tokenIds);
        }
    }
}
