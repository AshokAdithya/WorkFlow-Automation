package com.pixels.zapierClone.automation_platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pixels.zapierClone.automation_platform.entity.AppIntegration;

@Repository
public interface AppIntegrationRepository extends JpaRepository<AppIntegration, Long> {
    Optional<AppIntegration> findByIdentifier(String identifier);

    @Query("SELECT DISTINCT ai FROM AppIntegration ai LEFT JOIN FETCH ai.triggers LEFT JOIN FETCH ai.actions")
    List<AppIntegration> findAllWithTriggersAndActions();
}