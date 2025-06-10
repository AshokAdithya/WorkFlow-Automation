package com.pixels.zapierClone.automation_platform.integration;

import com.pixels.zapierClone.automation_platform.entity.Action;
import com.pixels.zapierClone.automation_platform.entity.Credential;

public interface AppIntegrationService {
    String getIdentifier();
    String executeAction(Action action, Credential credential, String triggerPayload) throws Exception;
}