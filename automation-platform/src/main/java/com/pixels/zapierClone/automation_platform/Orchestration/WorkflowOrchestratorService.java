// 

package com.pixels.zapierClone.automation_platform.Orchestration;

import jakarta.annotation.PostConstruct;
import com.pixels.zapierClone.automation_platform.entity.Action;
import com.pixels.zapierClone.automation_platform.entity.ActionExecutionLog;
import com.pixels.zapierClone.automation_platform.entity.Credential;
import com.pixels.zapierClone.automation_platform.entity.Trigger;
import com.pixels.zapierClone.automation_platform.entity.Workflow;
import com.pixels.zapierClone.automation_platform.integration.AppIntegrationService;
import com.pixels.zapierClone.automation_platform.repository.ActionExecutionLogRepository;
import com.pixels.zapierClone.automation_platform.repository.ActionRepository;
import com.pixels.zapierClone.automation_platform.repository.CredentialRepository;
import com.pixels.zapierClone.automation_platform.repository.TriggerRepository;
import com.pixels.zapierClone.automation_platform.repository.WorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class WorkflowOrchestratorService {

    @Autowired private TriggerRepository triggerRepo;
    @Autowired private WorkflowRepository workflowRepo;
    @Autowired private ActionRepository actionRepo;
    @Autowired private CredentialRepository credentialRepo;
    @Autowired private ActionExecutionLogRepository logRepo;

    @Autowired
    private ApplicationContext applicationContext;

    private Map<String, AppIntegrationService> integrationServices;

    @PostConstruct
    public void init() {
        integrationServices = applicationContext.getBeansOfType(AppIntegrationService.class)
                .values()
                .stream()
                .collect(Collectors.toMap(AppIntegrationService::getIdentifier, Function.identity()));
    }

    public void executeWorkflow(Long triggerId, String payloadJson) {
        Trigger trigger = triggerRepo.findById(triggerId)
            .orElseThrow(() -> new RuntimeException("Trigger not found: " + triggerId));
        Workflow wf = trigger.getWorkflow();

        if (wf == null || !wf.isEnabled()) {
            System.out.println("Workflow " + (wf != null ? wf.getId() : "null") + " not found or not enabled. Skipping execution.");
            return;
        }

        List<Action> actions = actionRepo.findByWorkflowIdOrderByStepOrder(wf.getId());

        for (Action action : actions) {
            try {
                Credential credential = credentialRepo
                    .findByUserIdAndAppIntegrationId(wf.getUser().getId(), action.getAppIntegration().getId())
                    .orElseThrow(() -> new RuntimeException("Credential not found for user " + wf.getUser().getId() + " and app " + action.getAppIntegration().getName()));

                AppIntegrationService service = integrationServices.get(action.getAppIntegration().getIdentifier());
                if (service == null) {
                    throw new RuntimeException("No integration service found for app: " + action.getAppIntegration().getIdentifier());
                }

                String response = service.executeAction(action, credential, payloadJson);

                ActionExecutionLog log = new ActionExecutionLog();
                log.setAction(action);
                log.setExecutedAt(Instant.now());
                log.setSuccess(true);
                log.setResponseJson(response);
                logRepo.save(log);

            } catch (Exception ex) {
                ActionExecutionLog log = new ActionExecutionLog();
                log.setAction(action);
                log.setExecutedAt(Instant.now());
                log.setSuccess(false);
                log.setErrorMessage(ex.getMessage());
                logRepo.save(log);
                System.err.println("Action execution failed for workflow " + wf.getId() + ", action " + action.getId() + ": " + ex.getMessage());
                break;
            }
        }
    }

    public void executeWorkflowFromWebhooks(Long workflowId) {
        Workflow wf = workflowRepo.findById(workflowId)
            .orElseThrow(() -> new RuntimeException("Workflow not found: " + workflowId));

        System.out.println("Workflow with " + workflowId + " is triggered using webhook");

        if (!wf.isEnabled()) return;

        executeWorkflow(wf.getTrigger().getId(), "{}");
    }
}