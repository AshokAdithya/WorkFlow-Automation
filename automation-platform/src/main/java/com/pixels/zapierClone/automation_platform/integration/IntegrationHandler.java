package com.pixels.zapierClone.automation_platform.integration;

import java.util.List;
import java.util.Map;

import com.pixels.zapierClone.automation_platform.dto.TriggerResult;
import com.pixels.zapierClone.automation_platform.entity.AppIntegration;
import com.pixels.zapierClone.automation_platform.entity.Credential;
import com.pixels.zapierClone.automation_platform.entity.User;

public interface IntegrationHandler {
    String getServiceIdentifier(); // e.g. "gmail", "slack"
    
    String getAuthorizationUrl(Long userId);

    Credential handleCallback(Long userId, String code, String redirectUri);

    TriggerResult isTriggerFired(Long triggerId,String triggerIdentifier, Map<String, Object> inputConfig, Credential credential);

    List<Map<String, String>> executeAction(String actionIdentifier, Map<String, Object> inputConfig, Credential credential);

    Credential refreshToken(User user, Credential oldCredential);
    
    AppIntegration getAppIntegration();

    boolean isAccessTokenValid(Credential credential);

}
