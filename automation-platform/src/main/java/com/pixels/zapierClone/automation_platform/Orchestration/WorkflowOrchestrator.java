package com.pixels.zapierClone.automation_platform.Orchestration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixels.zapierClone.automation_platform.dto.TriggerResult;
import com.pixels.zapierClone.automation_platform.entity.*;
import com.pixels.zapierClone.automation_platform.integration.IntegrationHandler;
import com.pixels.zapierClone.automation_platform.integration.IntegrationHandlerRegistry;
import com.pixels.zapierClone.automation_platform.repository.TriggerRepository;
import com.pixels.zapierClone.automation_platform.repository.WorkflowRepository;
import com.pixels.zapierClone.automation_platform.service.TokenService;
import com.pixels.zapierClone.automation_platform.service.utils.PlaceholderUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WorkflowOrchestrator {

    @Autowired private WorkflowRepository workflowRepository;
    @Autowired private IntegrationHandlerRegistry handlerRegistry;
    @Autowired private TokenService tokenService;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private TriggerRepository triggerRepository;

    public void runWorkflow(Long triggerId, TriggerResult triggerResult) {
        Workflow workflow = triggerRepository.findWorkflowByTriggerId(triggerId);

        User user = workflow.getUser();

        List<WorkflowStep> steps = workflow.getSteps();
        steps.sort(Comparator.comparingInt(WorkflowStep::getStepOrder)); // Ensure order

        // Process each trigger response separately
        for (Map<String, String> response : triggerResult.getData()) {
            Map<String, Map<String, String>> singleStepData = new HashMap<>();
            singleStepData.put("step0", response);

            for (int i = 1; i < steps.size(); i++) {
                WorkflowStep step = steps.get(i);
                if (!"action".equalsIgnoreCase(step.getType())) continue;

                Action action = step.getAction();
                ActionDefinition def = action.getActionDefinition();
                String appIdentifier = def.getAppIntegration().getIdentifier();

                Credential credential = tokenService.getValidCredential(user, appIdentifier);
                if (credential == null) {
                    System.out.println("Missing/expired credentials for step" + i);
                    break; // skip to next response
                }

                IntegrationHandler handler = handlerRegistry.getHandler(appIdentifier);

                Map<String, Object> rawInputConfig;
                try {
                    rawInputConfig = objectMapper.readValue(action.getInputConfig(), new TypeReference<>() {});
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

                Map<String, Object> resolvedConfig = PlaceholderUtils.resolveMapPlaceholders(rawInputConfig, singleStepData);

                try {
                    List<Map<String, String>> result = handler.executeAction(def.getActionTypeIdentifier(), resolvedConfig, credential);
                    singleStepData.put("step" + i, result != null && !result.isEmpty() ? result.get(0) : new HashMap<>());
                } catch (Exception e) {
                    System.out.println("Action failed at step" + i);
                    e.printStackTrace();
                    break;
                }
            }
        }

        System.out.println("Workflow executed for all responses.");
    }
}
