// package com.pixels.zapierClone.automation_platform.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.pixels.zapierClone.automation_platform.entity.Trigger;
// import com.pixels.zapierClone.automation_platform.repository.TriggerRepository;

// @RestController
// @RequestMapping("/api/triggers")
// public class TriggerController {

//     @Autowired private TriggerRepository triggerRepository;

//     @PostMapping
//     public ResponseEntity<Trigger> create(@RequestBody Trigger trigger) {
//         return ResponseEntity.ok(triggerRepository.save(trigger));
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<Trigger> get(@PathVariable Long id) {
//         return triggerRepository.findById(id)
//                 .map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }
// }

package com.pixels.zapierClone.automation_platform.controller;

import com.pixels.zapierClone.automation_platform.entity.Trigger;
import com.pixels.zapierClone.automation_platform.repository.TriggerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/triggers")
public class TriggerController {

    @Autowired private TriggerRepository triggerRepository;

    @PostMapping
    public ResponseEntity<Trigger> createTrigger(@RequestBody Trigger trigger) {
        return ResponseEntity.ok(triggerRepository.save(trigger));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trigger> updateTrigger(@PathVariable Long id, @RequestBody Trigger updatedTrigger) {
        return triggerRepository.findById(id)
                .map(existingTrigger -> {
                    existingTrigger.setTriggerTypeIdentifier(updatedTrigger.getTriggerTypeIdentifier());
                    existingTrigger.setConfigJson(updatedTrigger.getConfigJson());
                    existingTrigger.setName(updatedTrigger.getName());
                    existingTrigger.setDescription(updatedTrigger.getDescription());
                    return ResponseEntity.ok(triggerRepository.save(existingTrigger));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trigger> getTriggerById(@PathVariable Long id) {
        return triggerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrigger(@PathVariable Long id) {
        triggerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}