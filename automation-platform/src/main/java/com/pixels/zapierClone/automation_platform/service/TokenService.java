package com.pixels.zapierClone.automation_platform.service;

import com.pixels.zapierClone.automation_platform.entity.Credential;
import com.pixels.zapierClone.automation_platform.entity.User;
import com.pixels.zapierClone.automation_platform.integration.IntegrationHandler;
import com.pixels.zapierClone.automation_platform.integration.IntegrationHandlerRegistry;
import com.pixels.zapierClone.automation_platform.repository.CredentialRepository;
import com.pixels.zapierClone.automation_platform.security.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenService {

    @Autowired private CredentialRepository credentialRepository;
    @Autowired private IntegrationHandlerRegistry handlerRegistry;
    @Autowired private EmailService emailService;

    public Credential getValidCredential(User user, String serviceIdentifier) {
        IntegrationHandler handler = handlerRegistry.getHandler(serviceIdentifier);

        Optional<Credential> optional = credentialRepository.findByUserAndAppIntegration(
            user,
            handler.getAppIntegration()
        );


        if (optional.isEmpty()) {
            return null;
        }

        Credential credential = optional.get();

        if (handler.isAccessTokenValid(credential)) {
            return credential;
        }

        // Attempt to refresh
        try {
            Credential refreshed = handler.refreshToken(user, credential);
            if (refreshed != null && handler.isAccessTokenValid(refreshed)) {
                return refreshed;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Notify via Gmail
        emailService.sendNotificationForExpiredToken(user.getEmail(), serviceIdentifier);
        return null;
    }
}
