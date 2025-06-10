package com.pixels.zapierClone.automation_platform.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pixels.zapierClone.automation_platform.entity.AppIntegration;
import com.pixels.zapierClone.automation_platform.repository.AppIntegrationRepository;

@Service
public class AppIntegrationMetadataService {

    private final AppIntegrationRepository appIntegrationRepository;

    public AppIntegrationMetadataService(AppIntegrationRepository appIntegrationRepository) {
        this.appIntegrationRepository = appIntegrationRepository;
    }

    public Optional<AppIntegration> findById(Long id) {
        return appIntegrationRepository.findById(id);
    }

    public Optional<AppIntegration> findByIdentifier(String identifier) {
        return appIntegrationRepository.findByIdentifier(identifier);
    }

    public List<AppIntegration> findAll() {
        return appIntegrationRepository.findAll();
    }

    public AppIntegration saveAppIntegration(AppIntegration appIntegration) {
        return appIntegrationRepository.save(appIntegration);
    }

    public void deleteAppIntegration(Long id) {
        appIntegrationRepository.deleteById(id);
    }
}
