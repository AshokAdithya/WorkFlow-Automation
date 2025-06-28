package com.pixels.zapierClone.automation_platform.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixels.zapierClone.automation_platform.dto.WorkflowDTO;
import com.pixels.zapierClone.automation_platform.dto.WorkflowStepDTO;
import com.pixels.zapierClone.automation_platform.entity.Action;
import com.pixels.zapierClone.automation_platform.entity.ActionDefinition;
import com.pixels.zapierClone.automation_platform.entity.AppIntegration;
import com.pixels.zapierClone.automation_platform.entity.Credential;
import com.pixels.zapierClone.automation_platform.entity.Trigger;
import com.pixels.zapierClone.automation_platform.entity.TriggerDefinition;
import com.pixels.zapierClone.automation_platform.entity.User;
import com.pixels.zapierClone.automation_platform.entity.Workflow;
import com.pixels.zapierClone.automation_platform.entity.WorkflowStep;
import com.pixels.zapierClone.automation_platform.repository.ActionDefinitionRepository;
import com.pixels.zapierClone.automation_platform.repository.ActionRepository;
import com.pixels.zapierClone.automation_platform.repository.AppIntegrationRepository;
import com.pixels.zapierClone.automation_platform.repository.CredentialRepository;
import com.pixels.zapierClone.automation_platform.repository.TriggerDefinitionRepository;
import com.pixels.zapierClone.automation_platform.repository.TriggerRepository;
import com.pixels.zapierClone.automation_platform.repository.UserRepository;
import com.pixels.zapierClone.automation_platform.repository.WorkflowRepository;
import com.pixels.zapierClone.automation_platform.repository.WorkflowStepRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import net.minidev.json.JSONObject;

@Service
public class WorkflowService {
    
    @Autowired private WorkflowRepository workflowRepo;
    @Autowired private WorkflowStepRepository stepRepo;
    @Autowired private TriggerDefinitionRepository triggerDefRepo;
    @Autowired private ActionDefinitionRepository actionDefRepo;
    @Autowired private CredentialRepository credRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private AppIntegrationRepository appRepo;
    @Autowired private TriggerRepository triggerRepo;
    @Autowired private ActionRepository actionRepo;

    @Transactional
    public Workflow createOrUpdateWorkflow(Long userId, WorkflowDTO dto) {

        System.out.println(dto);
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Workflow wf = dto.getId() != null ?
            workflowRepo.findById(dto.getId()).orElse(new Workflow()) :
            new Workflow();

        wf.setUser(user);
        wf.setName(dto.getName());
        wf.setIsEnabled(dto.isEnabled());

        if (wf.isEnabled()) {
            for (WorkflowStepDTO step : dto.getSteps()) {
                AppIntegration app = appRepo.findById(step.getApp())
                    .orElseThrow(() -> new RuntimeException("App not found"));

                if("NONE".equals(app.getAuthType())) continue;
                credRepo.findByUserAndAppIntegration(user, app)
                    .orElseThrow(() -> new RuntimeException("Missing credentials for " + app.getName()));
            }
        }

        wf = workflowRepo.save(wf);
        stepRepo.deleteAllByWorkflow(wf);

        List<WorkflowStep> newSteps = new ArrayList<>();
        for (WorkflowStepDTO sd : dto.getSteps()) {
            WorkflowStep step = new WorkflowStep();
            step.setStepOrder(sd.getStepOrder());
            step.setType(sd.getType());
            step.setWorkflow(wf);

            AppIntegration app = appRepo.findById(sd.getApp())
                .orElseThrow(() -> new RuntimeException("App not found"));

            if ("trigger".equalsIgnoreCase(sd.getType())) {
                TriggerDefinition td = triggerDefRepo.getByIdAndAppIntegration(sd.getEvent(), app);
                Trigger tr = new Trigger();
                tr.setTriggerDefinition(td);
                tr.setUser(user);
                tr.setInputConfig(sd.getInputConfig()); 
                tr.setWebhookPath(sd.getWebhookUrl());
                triggerRepo.save(tr);
                step.setTrigger(tr);
            } else {
                ActionDefinition ad = actionDefRepo.getByIdAndAppIntegration(sd.getEvent(), app);
                Action ac = new Action();
                ac.setActionDefinition(ad);
                ac.setUser(user);
                ac.setInputConfig(sd.getInputConfig()); 
                actionRepo.save(ac);
                step.setAction(ac);
            }

            newSteps.add(step);
        }

        wf.setSteps(newSteps);
        return workflowRepo.save(wf);
    }

    public WorkflowDTO convertToDTO(Workflow workflow) {
        WorkflowDTO dto = new WorkflowDTO();
        dto.setId(workflow.getId());
        dto.setName(workflow.getName());
        dto.setEnabled(workflow.isEnabled());

        List<WorkflowStepDTO> stepDTOs = new ArrayList<>();
        for (WorkflowStep step : workflow.getSteps()) {
            WorkflowStepDTO stepDTO = new WorkflowStepDTO();
            stepDTO.setStepOrder(step.getStepOrder());
            stepDTO.setType(step.getType());

            if ("trigger".equalsIgnoreCase(step.getType()) && step.getTrigger() != null) {
                stepDTO.setApp(step.getTrigger().getTriggerDefinition().getAppIntegration().getId());
                stepDTO.setEvent(step.getTrigger().getTriggerDefinition().getId());
                stepDTO.setInputConfig(step.getTrigger().getInputConfig());
                stepDTO.setWebhookUrl(step.getTrigger().getWebhookPath());
            } else if (step.getAction() != null) {
                stepDTO.setApp(step.getAction().getActionDefinition().getAppIntegration().getId());
                stepDTO.setEvent(step.getAction().getActionDefinition().getId());
                stepDTO.setInputConfig(step.getAction().getInputConfig());
            }

            stepDTOs.add(stepDTO);
        }

        dto.setSteps(stepDTOs);
        return dto;
    }

    public List<Map<String,Object>> getAllWorkflows(Long userId){
        User user = userRepo.findById(userId).orElseThrow();
        List<Workflow> workflows = workflowRepo.getAllByUser(user);

        return workflows.stream().map(wf -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id",wf.getId());
            map.put("name", wf.getName());
            map.put("updatedAt", wf.getUpdatedAt());
            map.put("isEnabled", wf.isEnabled());
            List<String> logos = wf.getSteps().stream().map(step -> {
                if (step.getType().equals("trigger") && step.getTrigger() != null) {
                    return step.getTrigger().getTriggerDefinition().getAppIntegration().getLogoUrl();
                } else if (step.getType().equals("action") && step.getAction() != null) {
                    return step.getAction().getActionDefinition().getAppIntegration().getLogoUrl();
                }
                return null;
            }).filter(Objects::nonNull).distinct().toList();
            map.put("appLogos", logos);
            return map;
        }).toList();

    }

    public WorkflowDTO getWorkflow(Long userId, Long id) {
        User user = userRepo.findById(userId).orElseThrow();
        Workflow wf = workflowRepo.getByIdAndUser(id, user).orElseThrow(() -> new EntityNotFoundException("Workflow not found"));
        return convertToDTO(wf);
    }

    @Transactional
    public boolean toggleWorkflow(Long workflowId, Long userId) {
        User user = userRepo.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Workflow workflow = workflowRepo.findById(workflowId).orElseThrow(() -> new RuntimeException("Workflow not found"));

        if (!workflow.isEnabled()) {
            for (WorkflowStep step : workflow.getSteps()) {
                AppIntegration app = null;

                if ("trigger".equalsIgnoreCase(step.getType()) && step.getTrigger() != null) {
                    app = step.getTrigger().getTriggerDefinition().getAppIntegration();
                } else if ("action".equalsIgnoreCase(step.getType()) && step.getAction() != null) {
                    app = step.getAction().getActionDefinition().getAppIntegration();
                }

                if (app != null) {
                    if("NONE".equals(app.getAuthType())) continue;
                    boolean connected = credRepo.existsByUserAndAppIntegration(user, app);
                    if (!connected) {
                        throw new RuntimeException("Missing credentials for app: " + app.getName());
                    }
                }
            }
        }

        workflow.setIsEnabled(!workflow.isEnabled());
        workflowRepo.save(workflow);
        return workflow.isEnabled();
    }


}
