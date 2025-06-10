package com.pixels.zapierClone.automation_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pixels.zapierClone.automation_platform.entity.TriggerEventLog;

@Repository
public interface TriggerEventLogRepository extends JpaRepository<TriggerEventLog, Long> {
    List<TriggerEventLog> findByTriggerId(Long triggerId);
}