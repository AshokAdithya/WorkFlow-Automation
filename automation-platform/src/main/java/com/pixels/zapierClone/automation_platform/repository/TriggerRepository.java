package com.pixels.zapierClone.automation_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pixels.zapierClone.automation_platform.entity.Trigger;
import com.pixels.zapierClone.automation_platform.entity.Workflow;

@Repository
public interface TriggerRepository extends JpaRepository<Trigger, Long> { 
    @Query("""
        SELECT t FROM WorkflowStep ws
        JOIN ws.trigger t
        WHERE ws.workflow.isEnabled = true
    """)
    List<Trigger> getAllTriggersForPoll();

    @Query("""
        SELECT ws.workflow FROM WorkflowStep ws
        WHERE ws.trigger.id = :triggerId
    """)
    Workflow findWorkflowByTriggerId(Long triggerId);
}