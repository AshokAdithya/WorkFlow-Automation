// package com.pixels.zapierClone.automation_platform.service;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.stereotype.Service;

// import com.pixels.zapierClone.automation_platform.entity.Credential;
// import com.pixels.zapierClone.automation_platform.entity.User;
// import com.pixels.zapierClone.automation_platform.repository.CredentialRepository;

// @Service
// public class CredentialService {

//     private final CredentialRepository credentialRepository;

//     public CredentialService(CredentialRepository credentialRepository) {
//         this.credentialRepository = credentialRepository;
//     }

//     public Optional<Credential> findByUserIdAndAppIntegrationId(Long userId,Long appIntegrationId) {
//         return credentialRepository.findByUserIdAndAppIntegrationId(userId,appIntegrationId);
//     }

//     public Optional<Credential> findById(Long id) {
//         return credentialRepository.findById(id);
//     }

//     public Credential saveCredential(Credential credential) {
//         return credentialRepository.save(credential);
//     }

//     public void deleteCredential(Long id) {
//         credentialRepository.deleteById(id);
//     }
// }

