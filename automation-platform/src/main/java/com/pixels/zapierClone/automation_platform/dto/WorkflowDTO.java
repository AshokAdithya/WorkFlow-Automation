package com.pixels.zapierClone.automation_platform.dto;

import java.util.List;

public class WorkflowDTO {
    private Long id;
    private String name;
    private boolean enabled;
    private List<WorkflowStepDTO> steps;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }

    public List<WorkflowStepDTO> getSteps() {
        return steps;
    }

    public void setSteps(List<WorkflowStepDTO> steps) {
        this.steps = steps;
    }
}
