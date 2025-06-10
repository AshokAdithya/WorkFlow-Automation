// package com.pixels.zapierClone.automation_platform.controller;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.pixels.zapierClone.automation_platform.entity.Credential;
// import com.pixels.zapierClone.automation_platform.repository.CredentialRepository;

// @RestController
// @RequestMapping("/api/credentials")
// public class CredentialController {

//     @Autowired private CredentialRepository credentialRepository;

//     @PostMapping
//     public ResponseEntity<Credential> save(@RequestBody Credential credential) {
//         return ResponseEntity.ok(credentialRepository.save(credential));
//     }

//     @GetMapping("/{userId}/{appIntegrationId}")
//     public ResponseEntity<Optional<Credential>> getByUser(@PathVariable Long userId,@PathVariable Long appIntegrationId) {
//         return ResponseEntity.ok(credentialRepository.findByUserIdAndAppIntegrationId(userId, appIntegrationId));
//     }

// }

package com.pixels.zapierClone.automation_platform.controller;

import com.pixels.zapierClone.automation_platform.entity.Credential;
import com.pixels.zapierClone.automation_platform.repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/credentials")
public class CredentialController {

    @Autowired private CredentialRepository credentialRepository;

    @PostMapping
    public ResponseEntity<Credential> saveCredential(@RequestBody Credential credential) {
        return ResponseEntity.ok(credentialRepository.save(credential));
    }

    @GetMapping("/user/{userId}/app/{appIntegrationId}")
    public ResponseEntity<Credential> getCredentialByUserAndApp(
            @PathVariable Long userId,
            @PathVariable Long appIntegrationId) {
        return credentialRepository.findByUserIdAndAppIntegrationId(userId, appIntegrationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Credential>> getCredentialsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(credentialRepository.findByUserId(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCredential(@PathVariable Long id) {
        credentialRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
