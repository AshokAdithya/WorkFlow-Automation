package com.pixels.zapierClone.automation_platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pixels.zapierClone.automation_platform.entity.Credential;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {
    Optional<Credential> findByUserIdAndAppIntegrationId(Long userId, Long appIntegrationId);
    List<Credential> findByUserId(Long userId);
}