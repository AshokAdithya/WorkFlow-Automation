package com.pixels.zapierClone.automation_platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pixels.zapierClone.automation_platform.entity.Trigger;

@Repository
public interface TriggerRepository extends JpaRepository<Trigger, Long> {
    Optional<Trigger> findById(Long id);
    Optional<Trigger> findByWebhookPath(String webhookPath);
}