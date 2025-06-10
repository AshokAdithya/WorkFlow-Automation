    // package com.pixels.zapierClone.automation_platform.controller;

    // import java.time.Instant;
    // import java.util.List;

    // import org.springframework.beans.factory.annotation.Autowired;
    // import org.springframework.http.ResponseEntity;
    // import org.springframework.web.bind.annotation.GetMapping;
    // import org.springframework.web.bind.annotation.PathVariable;
    // import org.springframework.web.bind.annotation.PostMapping;
    // import org.springframework.web.bind.annotation.RequestBody;
    // import org.springframework.web.bind.annotation.RequestMapping;
    // import org.springframework.web.bind.annotation.RestController;

    // import com.pixels.zapierClone.automation_platform.entity.TriggerEventLog;
    // import com.pixels.zapierClone.automation_platform.repository.TriggerEventLogRepository;

    // @RestController
    // @RequestMapping("/api/trigger-logs")
    // public class TriggerEventLogController {

    //     @Autowired private TriggerEventLogRepository triggerLogRepo;

    //     @PostMapping
    //     public ResponseEntity<TriggerEventLog> log(@RequestBody TriggerEventLog log) {
    //         log.setTriggeredAt(Instant.now());
    //         return ResponseEntity.ok(triggerLogRepo.save(log));
    //     }

    //     @GetMapping("/trigger/{triggerId}")
    //     public ResponseEntity<List<TriggerEventLog>> getByTrigger(@PathVariable Long triggerId) {
    //         return ResponseEntity.ok(triggerLogRepo.findByTriggerId(triggerId));
    //     }
    // }

    package com.pixels.zapierClone.automation_platform.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pixels.zapierClone.automation_platform.entity.TriggerEventLog;
import com.pixels.zapierClone.automation_platform.repository.TriggerEventLogRepository;

@RestController
@RequestMapping("/api/trigger-logs")
public class TriggerEventLogController {

    @Autowired private TriggerEventLogRepository triggerLogRepo;

    @PostMapping
    public ResponseEntity<TriggerEventLog> logTriggerEvent(@RequestBody TriggerEventLog log) {
        log.setTriggeredAt(Instant.now());
        return ResponseEntity.ok(triggerLogRepo.save(log));
    }

    @GetMapping("/trigger/{triggerId}")
    public ResponseEntity<List<TriggerEventLog>> getByTrigger(@PathVariable Long triggerId) {
        return ResponseEntity.ok(triggerLogRepo.findByTriggerId(triggerId));
    }
}