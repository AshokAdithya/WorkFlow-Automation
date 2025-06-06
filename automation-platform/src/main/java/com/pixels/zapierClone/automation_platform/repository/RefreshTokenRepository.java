package com.pixels.zapierClone.automation_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pixels.zapierClone.automation_platform.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUserId(Long userId);
}
