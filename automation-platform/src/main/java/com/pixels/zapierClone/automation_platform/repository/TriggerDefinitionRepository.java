package com.pixels.zapierClone.automation_platform.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pixels.zapierClone.automation_platform.entity.ActionDefinition;
import com.pixels.zapierClone.automation_platform.entity.AppIntegration;
import com.pixels.zapierClone.automation_platform.entity.TriggerDefinition;

public interface TriggerDefinitionRepository extends JpaRepository<TriggerDefinition, Long> {
    TriggerDefinition getByIdAndAppIntegration(Long id,AppIntegration app);
}