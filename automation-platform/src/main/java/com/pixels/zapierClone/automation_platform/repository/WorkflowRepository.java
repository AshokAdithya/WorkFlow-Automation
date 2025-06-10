package com.pixels.zapierClone.automation_platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pixels.zapierClone.automation_platform.entity.Workflow;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, Long> {
    List<Workflow> findByUserId(Long userId);
    Optional<Workflow> findById(Long id); // Override to ensure it's Optional
}