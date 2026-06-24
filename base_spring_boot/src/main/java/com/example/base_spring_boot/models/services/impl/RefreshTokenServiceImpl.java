package com.example.base_spring_boot.models.services.impl;

import com.example.base_spring_boot.exceptions.HttpBadRequestException;
import com.example.base_spring_boot.models.entities.RefreshToken;
import com.example.base_spring_boot.models.entities.User;
import com.example.base_spring_boot.models.repositories.IRefreshTokenRepository;
import com.example.base_spring_boot.models.repositories.IUserRepository;
import com.example.base_spring_boot.models.services.IRefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements IRefreshTokenService {

    private final IRefreshTokenRepository refreshTokenRepository;
    private final IUserRepository userRepository;

    private final Long refreshTokenDurationMs =
            7 * 24 * 60 * 60 * 1000L;

    @Override
    public RefreshToken createRefreshToken(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new HttpBadRequestException("User not found"));

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(
                        Instant.now().plusMillis(refreshTokenDurationMs)
                )
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {

        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {

            refreshTokenRepository.delete(token);

            throw new HttpBadRequestException(
                    "Refresh token was expired"
            );
        }

        if (Boolean.TRUE.equals(token.getRevoked())) {
            throw new HttpBadRequestException(
                    "Refresh token was revoked"
            );
        }

        return token;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public void deleteByUserId(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new HttpBadRequestException("User not found"));

        refreshTokenRepository.deleteByUser(user);
    }
}