package com.pixels.zapierClone.automation_platform.integration.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixels.zapierClone.automation_platform.entity.Action;
import com.pixels.zapierClone.automation_platform.entity.Credential;
import com.pixels.zapierClone.automation_platform.integration.AppIntegrationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SlackIntegrationService implements AppIntegrationService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    public SlackIntegrationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getIdentifier() {
        return "slack";
    }

    @Override
    public String executeAction(Action action, Credential credential, String triggerPayload) throws Exception {
        JsonNode config = objectMapper.readTree(action.getConfigJson());

        switch (action.getActionTypeIdentifier()) {
            case "send_channel_message":
                String channel = config.has("channel") ? config.get("channel").asText() : null;
                String message = config.has("message_text") ? config.get("message_text").asText() : null;

                if (channel == null || message == null) {
                    throw new IllegalArgumentException("Slack 'send_channel_message' config missing channel or message.");
                }

                if (triggerPayload != null && !triggerPayload.isEmpty()) {
                    JsonNode payload = objectMapper.readTree(triggerPayload);
                    if (payload.has("data")) {
                        message += "\nTrigger Data: " + payload.get("data").asText();
                    }
                }

                System.out.println("Executing Slack send_channel_message action:");
                System.out.println("  Channel: " + channel);
                System.out.println("  Message: " + message);
                System.out.println("  Using Credential for App: " + credential.getAppIntegration().getName());
                return "{ \"ok\": true, \"message\": \"Message sent to Slack\" }";

            case "post_private_message": // Example of another Slack action
                String userId = config.has("user_id") ? config.get("user_id").asText() : null;
                String privateMessage = config.has("message_text") ? config.get("message_text").asText() : null;

                if (userId == null || privateMessage == null) {
                    throw new IllegalArgumentException("Slack 'post_private_message' config missing user_id or message.");
                }
                System.out.println("Executing Slack post_private_message action:");
                System.out.println("  User ID: " + userId);
                System.out.println("  Message: " + privateMessage);
                return "{ \"ok\": true, \"message\": \"Private message sent to Slack user\" }";

            default:
                throw new IllegalArgumentException("Unsupported Slack action type: " + action.getActionTypeIdentifier());
        }
    }
}
