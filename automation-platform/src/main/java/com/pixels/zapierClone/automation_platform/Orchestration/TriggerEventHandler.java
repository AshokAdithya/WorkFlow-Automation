// package com.pixels.zapierClone.automation_platform.Orchestration;

// import java.time.Instant;

// import com.pixels.zapierClone.automation_platform.repository.TriggerRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.pixels.zapierClone.automation_platform.entity.Trigger;
// import com.pixels.zapierClone.automation_platform.entity.TriggerEventLog;
// import com.pixels.zapierClone.automation_platform.repository.TriggerEventLogRepository;

// @RestController
// @RequestMapping("/catch/hooks")
// public class TriggerEventHandler {

//     @Autowired private TriggerEventLogRepository triggerEventLogRepo;
//     @Autowired private WorkflowOrchestratorService orchestrator;
//     @Autowired private TriggerRepository triggerRepo; 

//     @PostMapping("/{uniqueWebhookPath}")    
//     public ResponseEntity<String> receiveWebhookTrigger(
//         @PathVariable String uniqueWebhookPath, 
//         @RequestBody(required = false) String payloadJson 
//     ) {
//         Trigger trigger = triggerRepo.findByWebhookPath(uniqueWebhookPath)
//             .orElseThrow(() -> new RuntimeException("Webhook trigger not found for path: " + uniqueWebhookPath));

//         TriggerEventLog log = new TriggerEventLog();
//         log.setTrigger(trigger);
//         log.setTriggeredAt(Instant.now());
//         log.setPayloadJson(payloadJson != null ? payloadJson : "{}");

//         triggerEventLogRepo.save(log);
//         orchestrator.executeWorkflow(trigger.getId(), payloadJson != null ? payloadJson : "{}");

//         return ResponseEntity.ok("Workflow execution started by webhook: " + uniqueWebhookPath);
//     }

//     @GetMapping("/{workflowId}")
//     public void executeWorkflowFromWebhooks(@PathVariable Long workflowId){
//         orchestrator.executeWorkflowFromWebhooks(workflowId);
//     }
// }