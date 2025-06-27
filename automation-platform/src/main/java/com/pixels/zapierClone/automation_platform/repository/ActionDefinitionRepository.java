package com.pixels.zapierClone.automation_platform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pixels.zapierClone.automation_platform.entity.ActionDefinition;
import com.pixels.zapierClone.automation_platform.entity.AppIntegration;

public interface ActionDefinitionRepository extends JpaRepository<ActionDefinition, Long> {
    ActionDefinition getByIdAndAppIntegration(Long id,AppIntegration app);
}