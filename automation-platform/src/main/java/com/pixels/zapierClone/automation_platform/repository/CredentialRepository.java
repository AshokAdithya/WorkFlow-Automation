package com.pixels.zapierClone.automation_platform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pixels.zapierClone.automation_platform.entity.AppIntegration;
import com.pixels.zapierClone.automation_platform.entity.Credential;
import com.pixels.zapierClone.automation_platform.entity.User;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {
    Optional<Credential> findByUserAndAppIntegration(User user, AppIntegration integration);
    boolean existsByUserAndAppIntegration(User user,AppIntegration app);
    void deleteByUserAndAppIntegration(User user, AppIntegration app);
}