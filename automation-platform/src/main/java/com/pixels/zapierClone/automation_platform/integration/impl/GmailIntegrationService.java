package com.pixels.zapierClone.automation_platform.integration.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixels.zapierClone.automation_platform.entity.Action;
import com.pixels.zapierClone.automation_platform.entity.Credential;
import com.pixels.zapierClone.automation_platform.integration.AppIntegrationService;
import org.springframework.stereotype.Service;

@Service
public class GmailIntegrationService implements AppIntegrationService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getIdentifier() {
        return "gmail";
    }

    @Override
    public String executeAction(Action action, Credential credential, String triggerPayload) throws Exception {
        JsonNode config = objectMapper.readTree(action.getConfigJson());

        switch (action.getActionTypeIdentifier()) {
            case "send_email":
                String to = config.has("to") ? config.get("to").asText() : null;
                String subject = config.has("subject") ? config.get("subject").asText() : null;
                String body = config.has("body") ? config.get("body").asText() : null;

                if (to == null || subject == null || body == null) {
                    throw new IllegalArgumentException("Gmail 'send_email' action config missing 'to', 'subject', or 'body'.");
                }

                if (triggerPayload != null && !triggerPayload.isEmpty()) {
                    JsonNode payload = objectMapper.readTree(triggerPayload);
                    if (payload.has("sender")) {
                        body += "\n--- Original Trigger Sender: " + payload.get("sender").asText();
                    }
                }

                System.out.println("Executing Gmail send_email action:");
                System.out.println("  To: " + to);
                System.out.println("  Subject: " + subject);
                System.out.println("  Body: " + body);
                System.out.println("  Using Credential for App: " + credential.getAppIntegration().getName());
                return "{ \"status\": \"sent\", \"messageId\": \"dummy-id-123\" }";

            case "create_draft": // Example of another Gmail action
                String draftTo = config.has("to") ? config.get("to").asText() : null;
                String draftSubject = config.has("subject") ? config.get("subject").asText() : null;
                String draftBody = config.has("body") ? config.get("body").asText() : null;

                if (draftTo == null || draftSubject == null || draftBody == null) {
                    throw new IllegalArgumentException("Gmail 'create_draft' action config missing 'to', 'subject', or 'body'.");
                }
                System.out.println("Executing Gmail create_draft action:");
                System.out.println("  To: " + draftTo);
                System.out.println("  Subject: " + draftSubject);
                System.out.println("  Body: " + draftBody);
                return "{ \"status\": \"draft_created\", \"draftId\": \"dummy-draft-456\" }";

            default:
                throw new IllegalArgumentException("Unsupported Gmail action type: " + action.getActionTypeIdentifier());
        }
    }
}
