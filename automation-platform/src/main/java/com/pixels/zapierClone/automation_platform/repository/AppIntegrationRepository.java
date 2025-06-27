package com.pixels.zapierClone.automation_platform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pixels.zapierClone.automation_platform.entity.AppIntegration;
import java.util.List;


public interface AppIntegrationRepository extends JpaRepository<AppIntegration, Long> {
    Optional<AppIntegration> findByIdentifier(String identifier);
}