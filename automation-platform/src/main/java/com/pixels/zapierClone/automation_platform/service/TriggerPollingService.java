package com.pixels.zapierClone.automation_platform.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixels.zapierClone.automation_platform.entity.Credential;
import com.pixels.zapierClone.automation_platform.entity.Trigger;
import com.pixels.zapierClone.automation_platform.entity.TriggerDefinition;
import com.pixels.zapierClone.automation_platform.entity.User;
import com.pixels.zapierClone.automation_platform.integration.IntegrationHandler;
import com.pixels.zapierClone.automation_platform.integration.IntegrationHandlerRegistry;
import com.pixels.zapierClone.automation_platform.repository.TriggerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import com.pixels.zapierClone.automation_platform.Orchestration.WorkflowOrchestrator;
import com.pixels.zapierClone.automation_platform.dto.TriggerResult;

@Service
@EnableScheduling

public class TriggerPollingService {

    @Autowired private TriggerRepository triggerRepository;
    @Autowired private IntegrationHandlerRegistry handlerRegistry;
    @Autowired private TokenService tokenService;
    @Autowired private WorkflowOrchestrator orchestrator; 
    @Autowired private ObjectMapper objectMapper;

    @Transactional()
    @Scheduled(fixedRate = 90000) // Every 1 minute
    public void pollTriggers() {
        List<Trigger> triggers = triggerRepository.getAllTriggersForPoll();

        for (Trigger trigger : triggers) {
            TriggerDefinition def = trigger.getTriggerDefinition();
            User user = trigger.getUser();
            String appIdentifier = def.getAppIntegration().getIdentifier();

            Credential credential = tokenService.getValidCredential(user, appIdentifier);
            if (credential == null) continue;

            IntegrationHandler handler = handlerRegistry.getHandler(appIdentifier);

            Map<String, Object> inputConfig;
            try {
                inputConfig = objectMapper.readValue(trigger.getInputConfig(), Map.class);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            try {
                TriggerResult result = handler.isTriggerFired(trigger.getId(), def.getTriggerTypeIdentifier(), inputConfig, credential);
                if (result.isTriggered()) {
                    System.out.println("Hello");
                    orchestrator.runWorkflow(trigger.getId(), result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
