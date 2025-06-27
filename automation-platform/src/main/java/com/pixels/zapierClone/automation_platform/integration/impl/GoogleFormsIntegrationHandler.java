package com.pixels.zapierClone.automation_platform.integration.impl;

import com.pixels.zapierClone.automation_platform.dto.TriggerResult;
import com.pixels.zapierClone.automation_platform.entity.AppIntegration;
import com.pixels.zapierClone.automation_platform.entity.Credential;
import com.pixels.zapierClone.automation_platform.entity.User;
import com.pixels.zapierClone.automation_platform.integration.IntegrationHandler;
import com.pixels.zapierClone.automation_platform.repository.AppIntegrationRepository;
import com.pixels.zapierClone.automation_platform.repository.CredentialRepository;
import com.pixels.zapierClone.automation_platform.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pixels.zapierClone.automation_platform.service.TokenService;
import com.pixels.zapierClone.automation_platform.service.TriggerResponseTracker;

@Component
public class GoogleFormsIntegrationHandler implements IntegrationHandler {

    @Value("${googleforms.client-id}")
    private String clientId;

    @Value("${googleforms.client-secret}")
    private String clientSecret;

    @Value("${googleforms.redirect-uri}")
    private String redirectUri;

    @Autowired private CredentialRepository credentialRepository;
    @Autowired private AppIntegrationRepository appIntegrationRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private WebClient webClient;
    @Autowired private TriggerResponseTracker triggerResponseTracker;

    // private final Map<String, Instant> latestResponseTimestamps = new ConcurrentHashMap<>();

    @Override
    public String getServiceIdentifier() {
        return "google_forms";
    }

    @Override
    public String getAuthorizationUrl(Long userId) {
        return "https://accounts.google.com/o/oauth2/v2/auth"
             + "?client_id=" + clientId
             + "&redirect_uri=" + redirectUri
             + "&response_type=code"
             + "&scope=https://www.googleapis.com/auth/forms.body.readonly%20https://www.googleapis.com/auth/forms.responses.readonly"
             + "&access_type=offline"
             + "&prompt=consent"
             + "&state=" + userId;
    }

    @Override
    @Transactional
    public Credential handleCallback(Long userId, String code, String redirectUri) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("code", code);
        formData.add("redirect_uri", redirectUri);
        formData.add("grant_type", "authorization_code");

        Map<String, Object> tokenResponse = webClient.post()
            .uri("https://oauth2.googleapis.com/token")
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .body(BodyInserters.fromFormData(formData))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .block();

        if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
            throw new RuntimeException("Google Forms OAuth failed: No access token in response");
        }

        String accessToken = (String) tokenResponse.get("access_token");
        String refreshToken = (String) tokenResponse.get("refresh_token");
        Integer expiresIn = (Integer) tokenResponse.get("expires_in");

        AppIntegration formApp = appIntegrationRepository.findByIdentifier("google_forms").orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        credentialRepository.deleteByUserAndAppIntegration(user, formApp);

        Credential credential = new Credential();
        credential.setAppIntegration(formApp);
        credential.setUser(user);
        credential.setAccessToken(accessToken);
        credential.setRefreshToken(refreshToken);
        credential.setExpiresAt(Instant.now().plusSeconds(expiresIn));

        return credentialRepository.save(credential);
    }

    @Override
    public List<Map<String, String>> executeAction(String actionIdentifier, Map<String, Object> inputConfig, Credential credential) {
        Map<String,String> temp = new HashMap<>();
        return List.of(temp);
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
    @Transactional
    public Credential refreshToken(User user, Credential oldCredential) {
        if (oldCredential.getRefreshToken() == null) return null;

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token", oldCredential.getRefreshToken());
        formData.add("grant_type", "refresh_token");

        try{
            System.out.println("hasdfasiii");
            Map<String, Object> tokenResponse = webClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
                System.out.println("hiii");
                credentialRepository.deleteById(oldCredential.getId());
                return null;
            }

            String newAccessToken = (String) tokenResponse.get("access_token");
            Integer expiresIn = (Integer) tokenResponse.get("expires_in");

            oldCredential.setAccessToken(newAccessToken);
            oldCredential.setExpiresAt(Instant.now().plusSeconds(expiresIn));
            return credentialRepository.save(oldCredential);
        } catch (Exception e) {
            System.out.println("hiasldkfjii");
            System.out.println(oldCredential.getId());
            credentialRepository.deleteById(oldCredential.getId());
            return null;
        }
    }

    public Map<String, String> getQuestionIdToTitleMap(String formId, String accessToken) {
        WebClient webClient = WebClient.builder().build();

        Map<String, Object> response = webClient.get()
            .uri("https://forms.googleapis.com/v1/forms/" + formId)
            .headers(h -> h.setBearerAuth(accessToken))
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
            .block();

        Map<String, String> questionMap = new HashMap<>();

        if (response != null && response.containsKey("items")) {
            List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");

            for (Map<String, Object> item : items) {
                Map<String, Object> questionItem = (Map<String, Object>) item.get("questionItem");
                if (questionItem != null) {
                    Map<String, Object> question = (Map<String, Object>) questionItem.get("question");
                    if (question != null) {
                        String questionId = (String) question.get("questionId");
                        String title = (String) item.get("title");
                        if (questionId != null && title != null) {
                            questionMap.put(questionId, title);
                        }
                    }
                }
            }
        }

        return questionMap;
    }


    @Override
    public TriggerResult isTriggerFired(Long triggerId,String triggerIdentifier, Map<String, Object> inputConfig, Credential credential) {
        String formId = (String) inputConfig.get("spreadsheet_id");
        if (formId == null) return new TriggerResult(false, List.of());

        List<Map<String, String>> triggerDataList = new ArrayList<>();
        Map<String, String> questionMap = getQuestionIdToTitleMap(formId, credential.getAccessToken());

        try {
            Map<String, Object> response = webClient.get()
                .uri("https://forms.googleapis.com/v1/forms/" + formId + "/responses")
                .headers(headers -> headers.setBearerAuth(credential.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();

            if (response == null || !response.containsKey("responses")) return new TriggerResult(false, List.of());

            List<Map<String, Object>> responses = (List<Map<String, Object>>) response.get("responses");
            if (responses.isEmpty()) return new TriggerResult(false, List.of());

            Instant lastProcessedTime = triggerResponseTracker.getLastProcessedTime(triggerId);
            Instant maxSeenTime = lastProcessedTime;

            for (Map<String, Object> res : responses) {
                String timeStr = (String) res.get("lastSubmittedTime");
                if (timeStr == null) continue;

                Instant responseTime = Instant.parse(timeStr);
                if (!responseTime.isAfter(lastProcessedTime)) continue;

                Map<String, Object> answers = (Map<String, Object>) res.get("answers");
                Map<String, String> parsedAnswers = new LinkedHashMap<>();

                for (Map.Entry<String, Object> entry : answers.entrySet()) {
                    String questionId = entry.getKey();
                    Map<String, Object> answerData = (Map<String, Object>) entry.getValue();
                    Map<String, Object> textAnswers = (Map<String, Object>) answerData.get("textAnswers");

                    if (textAnswers != null && textAnswers.containsKey("answers")) {
                        List<Map<String, Object>> values = (List<Map<String, Object>>) textAnswers.get("answers");
                        if (!values.isEmpty()) {
                            String value = (String) values.get(0).get("value");
                            String title = questionMap.getOrDefault(questionId, "Unknown");
                            parsedAnswers.put(title, value);
                        }
                    }
                }

                if (!parsedAnswers.isEmpty()) {
                    triggerDataList.add(parsedAnswers);
                    if (responseTime.isAfter(maxSeenTime)) {
                        maxSeenTime = responseTime;
                    }
                }
            }

            if (!triggerDataList.isEmpty()) {
                triggerResponseTracker.updateLastProcessedTime(triggerId, maxSeenTime);
                System.out.println(triggerDataList);
                return new TriggerResult(true, triggerDataList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new TriggerResult(false, List.of());
    }

    @Override
    public AppIntegration getAppIntegration() {
        return appIntegrationRepository.findByIdentifier("google_forms").orElseThrow();
    }
}
