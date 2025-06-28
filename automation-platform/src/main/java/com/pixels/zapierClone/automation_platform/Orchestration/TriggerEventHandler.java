package com.pixels.zapierClone.automation_platform.Orchestration;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pixels.zapierClone.automation_platform.repository.TriggerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pixels.zapierClone.automation_platform.entity.Trigger;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixels.zapierClone.automation_platform.Orchestration.WorkflowOrchestrator;
import com.pixels.zapierClone.automation_platform.dto.TriggerResult;

@RestController
@RequestMapping("/catch/hooks")
public class TriggerEventHandler {

    @Autowired private WorkflowOrchestrator orchestrator;
    @Autowired private TriggerRepository triggerRepo; 

    @PostMapping("/{uniqueWebhookPath}")    
    public ResponseEntity<String> receiveWebhookTrigger(
        @PathVariable String uniqueWebhookPath, 
        @RequestBody(required = false) String payloadJson 
    ) {

        try {
            Trigger trigger = triggerRepo.findByWebhookPath(uniqueWebhookPath)
                .orElseThrow(() -> new RuntimeException("Invalid webhook path"));

            Map<String, String> data = new HashMap<>();
            if (payloadJson != null && !payloadJson.isBlank()) {
                ObjectMapper mapper = new ObjectMapper();
                data = mapper.readValue(payloadJson, new TypeReference<>() {});
            }

            TriggerResult triggerResult = new TriggerResult(true, List.of(data));

            orchestrator.runWorkflow(trigger.getId(), triggerResult);

            return ResponseEntity.ok("Webhook received and workflow triggered.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to trigger workflow: " + e.getMessage());
        }
    }

}