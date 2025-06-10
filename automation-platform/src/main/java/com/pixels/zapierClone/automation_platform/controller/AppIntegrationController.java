// package com.pixels.zapierClone.automation_platform.controller;

// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.pixels.zapierClone.automation_platform.entity.AppIntegration;
// import com.pixels.zapierClone.automation_platform.repository.AppIntegrationRepository;
// import com.pixels.zapierClone.automation_platform.service.AppIntegrationMetadataService;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;


// @RestController
// @RequestMapping("/api/app-integration")
// public class AppIntegrationController {
    
//     @Autowired
//     private AppIntegrationMetadataService appIntegrationService;

//     @PostMapping
//     private ResponseEntity<AppIntegration> saveAppIntegration(@RequestBody AppIntegration appIntegration){
//         return ResponseEntity.ok(appIntegrationService.saveAppIntegration(appIntegration));
//     }

//     @GetMapping("/{appIntegrationId}")
//     public ResponseEntity<Optional<AppIntegration>> getByAppIntegrationId(@RequestParam Long appIntegrationId) {
//         return ResponseEntity.ok(appIntegrationService.findById(appIntegrationId));
//     }
    
// }

package com.pixels.zapierClone.automation_platform.controller;

import com.pixels.zapierClone.automation_platform.entity.AppIntegration;
import com.pixels.zapierClone.automation_platform.service.AppIntegrationMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/app-integrations")
public class AppIntegrationController {

    @Autowired
    private AppIntegrationMetadataService appIntegrationMetadataService;

    @PostMapping
    public ResponseEntity<AppIntegration> saveAppIntegration(@RequestBody AppIntegration appIntegration){
        return ResponseEntity.ok(appIntegrationMetadataService.saveAppIntegration(appIntegration));
    }

    @GetMapping
    public ResponseEntity<List<AppIntegration>> getAllAppIntegrations() {
        return ResponseEntity.ok(appIntegrationMetadataService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppIntegration> getAppIntegrationById(@PathVariable Long id) {
        return appIntegrationMetadataService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
