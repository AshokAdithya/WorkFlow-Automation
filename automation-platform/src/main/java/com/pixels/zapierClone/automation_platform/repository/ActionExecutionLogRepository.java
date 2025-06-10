package com.pixels.zapierClone.automation_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pixels.zapierClone.automation_platform.entity.ActionExecutionLog;


@Repository
public interface ActionExecutionLogRepository extends JpaRepository<ActionExecutionLog, Long> {
    List<ActionExecutionLog> findByActionId(Long actionId);
}
