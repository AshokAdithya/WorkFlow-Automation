package com.pixels.zapierClone.automation_platform.integration.impl;

import com.pixels.zapierClone.automation_platform.dto.TriggerResult;
import com.pixels.zapierClone.automation_platform.entity.AppIntegration;
import com.pixels.zapierClone.automation_platform.entity.Credential;
import com.pixels.zapierClone.automation_platform.entity.User;
import com.pixels.zapierClone.automation_platform.integration.IntegrationHandler;
import com.pixels.zapierClone.automation_platform.repository.AppIntegrationRepository;
import com.pixels.zapierClone.automation_platform.repository.CredentialRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.pixels.zapierClone.automation_platform.repository.UserRepository;

import jakarta.transaction.Transactional;

@Component
public class GmailIntegrationHandler implements IntegrationHandler {

    @Value("${gmail.client-id}")
    private String clientId;

    @Value("${gmail.client-secret}")
    private String clientSecret;

    @Value("${gmail.redirect-uri}")
    private String redirectUri;

    @Autowired private CredentialRepository credentialRepository;
    @Autowired private AppIntegrationRepository appIntegrationRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private WebClient webClient;


    @Override
    public String getServiceIdentifier() {
        return "gmail";
    }

    @Override
    public String getAuthorizationUrl(Long userId) {
        return "https://accounts.google.com/o/oauth2/v2/auth"
            + "?client_id=" + clientId
            + "&redirect_uri=" + redirectUri
            + "&response_type=code"
            + "&scope=https://www.googleapis.com/auth/gmail.send%20https://www.googleapis.com/auth/gmail.metadata"
            + "&access_type=offline"
            + "&prompt=consent"
            + "&state=" + userId;
    }

    @Override
    @Transactional
    public Credential handleCallback(Long userId, String code, String redirectUri) {

        MultiValueMap<String,String> formData = new LinkedMultiValueMap<>();
        formData.add("code",code);
        formData.add("client_id",clientId);
        formData.add("client_secret",clientSecret);
        formData.add("redirect_uri",redirectUri);
        formData.add("grant_type","authorization_code");

        Map<String,Object> data = webClient.post()
            .uri("https://oauth2.googleapis.com/token")
            .header(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .body(BodyInserters.fromFormData(formData))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String,Object>>(){})
            .block();

        if (data == null || !data.containsKey("access_token")) {
            throw new RuntimeException("Slack OAuth failed: No access token in response");
        }

        System.out.println(data);

        String accessToken = (String) data.get("access_token");
        String refreshToken = (String) data.get("refresh_token");
        Integer expiresIn = (Integer) data.get("expires_in");

        AppIntegration gmailApp = appIntegrationRepository.findByIdentifier("gmail").orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        credentialRepository.deleteByUserAndAppIntegration(user, gmailApp);

        Credential credential = new Credential();
        credential.setUser(user);
        credential.setAppIntegration(gmailApp);
        credential.setAccessToken(accessToken);
        credential.setRefreshToken(refreshToken);
        credential.setExpiresAt(Instant.now().plusSeconds(expiresIn));

        return credentialRepository.save(credential);
    }

    @Override
    public TriggerResult isTriggerFired(Long triggerId,String triggerIdentifier, Map<String, Object> inputConfig, Credential credential) {
        throw new IllegalArgumentException("Unsupported service: " + triggerIdentifier);
    }

    @Override
    public List<Map<String, String>> executeAction(String actionIdentifier, Map<String, Object> resolvedConfig, Credential credential) {

        return switch (actionIdentifier){
            case "send_email" ->sendEmail(resolvedConfig,credential);
            default -> throw new IllegalArgumentException("Unsupported Gmail action: " + actionIdentifier);
        };
    }

    public List<Map<String,String>> sendEmail(Map<String, Object> resolvedConfig, Credential credential){
        String accessToken = credential.getAccessToken();
        String to = (String) resolvedConfig.get("to");
        String subject = (String) resolvedConfig.get("subject");
        String body = (String) resolvedConfig.get("body");

        try {
            WebClient client = WebClient.create();

            Map<String, Object> messagePayload = Map.of(
                "raw", Base64.getUrlEncoder().encodeToString(
                    ("To: " + to + "\r\n" +
                    "Subject: " + subject + "\r\n" +
                    "Content-Type: text/plain; charset=\"UTF-8\"\r\n\r\n" +
                    body).getBytes("UTF-8")
                )
            );

            client.post()
                .uri("https://gmail.googleapis.com/gmail/v1/users/me/messages/send")
                .headers(h -> h.setBearerAuth(accessToken))
                .bodyValue(messagePayload)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // Synchronous

            Map<String, String> result = new HashMap<>();
            return List.of(result);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send Gmail", e);
        }
    }


    @Override
    public boolean isAccessTokenValid(Credential credential) {
        try {
            webClient.get()
                .uri("https://www.googleapis.com/oauth2/v3/tokeninfo?access_token=" + credential.getAccessToken())
                .retrieve()
                .bodyToMono(Map.class)
                .block();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Credential refreshToken(User user, Credential oldCredential) {
        if (oldCredential.getRefreshToken() == null) return null;

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token", oldCredential.getRefreshToken());
        formData.add("grant_type", "refresh_token");

        try{
            Map<String, Object> tokenResponse = webClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
                credentialRepository.deleteById(oldCredential.getId());
                return null;
            }

            String newAccessToken = (String) tokenResponse.get("access_token");
            Integer expiresIn = (Integer) tokenResponse.get("expires_in");

            oldCredential.setAccessToken(newAccessToken);
            oldCredential.setExpiresAt(Instant.now().plusSeconds(expiresIn));
            Credential newCredential = credentialRepository.save(oldCredential);
            return newCredential;
        } catch (Exception e) {
            credentialRepository.deleteById(oldCredential.getId());
            return null;
        }
    }

@Override
public AppIntegration getAppIntegration() {
    return appIntegrationRepository.findByIdentifier("gmail").orElseThrow();
}

}

