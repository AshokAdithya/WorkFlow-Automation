package com.pixels.zapierClone.automation_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pixels.zapierClone.automation_platform.entity.Workflow;
import com.pixels.zapierClone.automation_platform.entity.WorkflowStep;

@Repository
public interface WorkflowStepRepository extends JpaRepository<WorkflowStep, Long> { 
    void deleteAllByWorkflow(Workflow wf);
}