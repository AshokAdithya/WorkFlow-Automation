package com.pixels.zapierClone.automation_platform.integration.impl;

import com.pixels.zapierClone.automation_platform.entity.Credential;
import com.pixels.zapierClone.automation_platform.entity.User;
import com.pixels.zapierClone.automation_platform.integration.IntegrationHandler;
import com.pixels.zapierClone.automation_platform.repository.CredentialRepository;
import com.pixels.zapierClone.automation_platform.entity.AppIntegration;
import com.pixels.zapierClone.automation_platform.repository.AppIntegrationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pixels.zapierClone.automation_platform.dto.TriggerResult;
import com.pixels.zapierClone.automation_platform.repository.UserRepository;

import jakarta.transaction.Transactional;

@Component
public class SlackIntegrationHandler implements IntegrationHandler {

    @Value("${slack.client-id}")
    private String clientId;

    @Value("${slack.client-secret}")
    private String clientSecret;

    @Value("${slack.redirect-uri}")
    private String redirectUri;

    @Autowired private CredentialRepository credentialRepository;
    @Autowired private AppIntegrationRepository appIntegrationRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private WebClient webClient;


    @Override
    public String getServiceIdentifier() {
        return "slack";
    }

    @Override
    public String getAuthorizationUrl(Long userId) {
        return "https://slack.com/oauth/v2/authorize" +
            "?client_id=" + clientId +
            "&scope=chat:write" +                                 // Optional: for bot actions
            "&user_scope=chat:write,channels:read,users:read" +   // 🔥 User-level permissions
            "&redirect_uri=" + redirectUri +
            "&state=" + userId;
    }

    @Override
    @Transactional
    public Credential handleCallback(Long userId, String code, String redirectUri) {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("code", code);
        formData.add("redirect_uri", redirectUri);

        Map<String, Object> data = webClient.post()
            .uri("https://slack.com/api/oauth.v2.access")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .body(BodyInserters.fromFormData(formData))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .block();

        if (data == null || !data.containsKey("access_token")) {
            throw new RuntimeException("Slack OAuth failed: No access token in response");
        }

        String accessToken = (String) data.get("access_token");
        String externalId = (String) ((Map<String, Object>) data.get("authed_user")).get("id");

        AppIntegration slackApp = appIntegrationRepository.findByIdentifier("slack").orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        credentialRepository.deleteByUserAndAppIntegration(user, slackApp);

        Credential credential = new Credential();
        credential.setAppIntegration(slackApp);
        credential.setUser(user);
        credential.setAccessToken(accessToken);
        credential.setExternalAccountId(externalId);
        credential.setExpiresAt(Instant.now().plusSeconds(3600));

        return credentialRepository.save(credential);
    }

    @Override
    public TriggerResult isTriggerFired(Long triggerId,String triggerIdentifier, Map<String, Object> inputConfig, Credential credential) {
        return new TriggerResult(false,List.of()); 
    }

    @Override
    public List<Map<String, String>> executeAction(String actionIdentifier, Map<String, Object> inputConfig, Credential credential) {
        return switch (actionIdentifier){
            case "send_slack_message" ->sendMessageToChannelAsBot(inputConfig,credential);
            default -> throw new IllegalArgumentException("Unsupported Gmail action: " + actionIdentifier);
        };
    }

    public List<Map<String,String>> sendMessageToChannelAsBot(Map<String, Object> inputConfig, Credential credential){
        String channel = (String) inputConfig.get("channel");
        String message = (String) inputConfig.get("message");

        if (channel == null || channel.isBlank() || message == null || message.isBlank()) {
            throw new IllegalArgumentException("Missing required fields: 'channel' or 'message'");
        }

        try {
            Map<String, Object> payload = Map.of(
                "channel", channel,
                "text", message
            );

            Map<String, Object> response = webClient.post()
                .uri("https://slack.com/api/chat.postMessage")
                .headers(headers -> {
                    headers.setBearerAuth(credential.getAccessToken());
                    headers.setContentType(MediaType.APPLICATION_JSON);
                })
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

            if (response == null || !Boolean.TRUE.equals(response.get("ok"))) {
                throw new RuntimeException("Slack API error: " + (response != null ? response.get("error") : "null response"));
            }

            // Optional: Get ts and channel from response
            String ts = (String) response.get("ts"); // message timestamp
            String sentChannel = (String) response.get("channel");

            Map<String, String> result = new HashMap<>();
            result.put("status", "sent");
            result.put("channel", sentChannel);
            result.put("ts", ts);
            result.put("message", message);

            return List.of(result);
        } catch (Exception e) {
            throw new RuntimeException("Slack message failed", e);
        }
    }

    @Override
    public boolean isAccessTokenValid(Credential credential) {
        try {
            webClient.get()
                .uri("https://slack.com/api/auth.test")
                .headers(h -> h.setBearerAuth(credential.getAccessToken()))
                .retrieve()
                .bodyToMono(Map.class)
                .block();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Credential refreshToken(User user, Credential credential) {
        if (credential.getRefreshToken() == null) return null;

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("grant_type", "refresh_token");
        formData.add("refresh_token", credential.getRefreshToken());

        try {
            Map<String, Object> data = webClient.post()
                .uri("https://slack.com/api/oauth.v2.access")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

            if (data == null || !Boolean.TRUE.equals(data.get("ok"))){
                credentialRepository.deleteById(credential.getId());
                return null;
            }

            credential.setAccessToken((String) data.get("access_token"));
            return credentialRepository.save(credential);
        } catch (Exception e) {
            credentialRepository.deleteById(credential.getId());
            return null;
        }
    }

    @Override
    public AppIntegration getAppIntegration() {
        return appIntegrationRepository.findByIdentifier("slack").orElseThrow();
    }


}
