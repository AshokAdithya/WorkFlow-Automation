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
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import com.pixels.zapierClone.automation_platform.entity.Workflow;
// import com.pixels.zapierClone.automation_platform.repository.WorkflowRepository;
// import com.pixels.zapierClone.automation_platform.service.WorkflowService;

// @RestController
// @RequestMapping("/api/workflows")
// public class WorkflowController {

//     @Autowired private WorkflowRepository workflowRepository;

//     @PostMapping
//     public ResponseEntity<Workflow> create(@RequestBody Workflow workflow) {
//         workflow.setCreatedAt(Instant.now());
//         workflow.setUpdatedAt(Instant.now());
//         return ResponseEntity.ok(workflowRepository.save(workflow));
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<Workflow> get(@PathVariable Long id) {
//         return workflowRepository.findById(id)
//                 .map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }

//     @GetMapping("/user/{userId}")
//     public ResponseEntity<List<Workflow>> getByUser(@PathVariable Long userId) {
//         return ResponseEntity.ok(workflowRepository.findByUserId(userId));
//     }
// }


package com.pixels.zapierClone.automation_platform.controller;

import com.pixels.zapierClone.automation_platform.entity.Workflow;
import com.pixels.zapierClone.automation_platform.repository.WorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {

    @Autowired private WorkflowRepository workflowRepository;

    @PostMapping
    public ResponseEntity<Workflow> createWorkflow(@RequestBody Workflow workflow) {
        workflow.setCreatedAt(Instant.now());
        workflow.setUpdatedAt(Instant.now());
        return ResponseEntity.ok(workflowRepository.save(workflow));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Workflow> updateWorkflow(@PathVariable Long id, @RequestBody Workflow updatedWorkflow) {
        return workflowRepository.findById(id)
                .map(existingWorkflow -> {
                    existingWorkflow.setName(updatedWorkflow.getName());
                    existingWorkflow.setEnabled(updatedWorkflow.isEnabled());
                    existingWorkflow.setUpdatedAt(Instant.now());
                    return ResponseEntity.ok(workflowRepository.save(existingWorkflow));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Workflow> getWorkflowById(@PathVariable Long id) {
        return workflowRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Workflow>> getWorkflowsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(workflowRepository.findByUserId(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkflow(@PathVariable Long id) {
        workflowRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}