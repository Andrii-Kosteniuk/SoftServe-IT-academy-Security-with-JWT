package com.softserve.itacademy.todolist.config.auth;

import com.softserve.itacademy.todolist.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@Service
@RequiredArgsConstructor
public class TokenCleaner {

    private final TokenRepository  tokenRepository;

    @Scheduled(fixedRate = 3600000) // Every hour
    public void cleanUpExpiredRevokedTokens() {
        tokenRepository.deleteRevokedTokens();
    }
}
