package com.pixels.zapierClone.automation_platform.controller;

import com.pixels.zapierClone.automation_platform.entity.AppIntegration;
import com.pixels.zapierClone.automation_platform.repository.AppIntegrationRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.pixels.zapierClone.automation_platform.dto.AppIntegrationDTO;
import com.pixels.zapierClone.automation_platform.entity.User;
import com.pixels.zapierClone.automation_platform.repository.CredentialRepository;
import com.pixels.zapierClone.automation_platform.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/integrations")
public class AppIntegrationController {

    @Autowired private UserRepository userRepo;
    @Autowired private AppIntegrationRepository appIntegrationRepo;
    @Autowired private CredentialRepository credentialRepo;

    @GetMapping
    public List<AppIntegrationDTO> getAllIntegrations(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        User user = userRepo.findById(userId).orElseThrow();

        List<AppIntegration> allIntegrations = appIntegrationRepo.findAll();

        return allIntegrations.stream()
                .map((app)->{
                    boolean connected = credentialRepo.existsByUserAndAppIntegration(user,app);
                    return new AppIntegrationDTO(
                        app.getId(),
                        app.getName(),
                        app.getIdentifier(),
                        app.getDescription(),
                        app.getLogoUrl() ,
                        app.getAuthType(),
                        connected,
                        app.getActionDefinitions(),
                        app.getTriggerDefinitions()
                    );
                }).collect(Collectors.toList());
    }
}