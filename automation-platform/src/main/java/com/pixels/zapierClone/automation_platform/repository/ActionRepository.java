package com.pixels.zapierClone.automation_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pixels.zapierClone.automation_platform.entity.Action;

@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {
    List<Action> findByWorkflowIdOrderByStepOrder(Long workflowId);
}