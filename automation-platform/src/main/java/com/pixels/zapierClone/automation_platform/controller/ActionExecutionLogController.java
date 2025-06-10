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

// import com.pixels.zapierClone.automation_platform.entity.ActionExecutionLog;
// import com.pixels.zapierClone.automation_platform.repository.ActionExecutionLogRepository;

// @RestController
// @RequestMapping("/api/action-logs")
// public class ActionExecutionLogController {

//     @Autowired private ActionExecutionLogRepository actionLogRepo;

//     @PostMapping
//     public ResponseEntity<ActionExecutionLog> log(@RequestBody ActionExecutionLog log) {
//         log.setExecutedAt(Instant.now());
//         return ResponseEntity.ok(actionLogRepo.save(log));
//     }

//     @GetMapping("/action/{actionId}")
//     public ResponseEntity<List<ActionExecutionLog>> getByAction(@PathVariable Long actionId) {
//         return ResponseEntity.ok(actionLogRepo.findByActionId(actionId));
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

import com.pixels.zapierClone.automation_platform.entity.ActionExecutionLog;
import com.pixels.zapierClone.automation_platform.repository.ActionExecutionLogRepository;

@RestController
@RequestMapping("/api/action-logs")
public class ActionExecutionLogController {

    @Autowired private ActionExecutionLogRepository actionLogRepo;

    @PostMapping
    public ResponseEntity<ActionExecutionLog> logActionExecution(@RequestBody ActionExecutionLog log) {
        log.setExecutedAt(Instant.now());
        return ResponseEntity.ok(actionLogRepo.save(log));
    }

    @GetMapping("/action/{actionId}")
    public ResponseEntity<List<ActionExecutionLog>> getByAction(@PathVariable Long actionId) {
        return ResponseEntity.ok(actionLogRepo.findByActionId(actionId));
    }
}