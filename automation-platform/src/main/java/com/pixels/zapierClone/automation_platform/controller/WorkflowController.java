package com.pixels.zapierClone.automation_platform.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pixels.zapierClone.automation_platform.dto.WorkflowDTO;
import com.pixels.zapierClone.automation_platform.dto.WorkflowStepDTO;
import com.pixels.zapierClone.automation_platform.entity.Workflow;
import com.pixels.zapierClone.automation_platform.repository.UserRepository;
import com.pixels.zapierClone.automation_platform.service.WorkflowService;

import jakarta.persistence.PostUpdate;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {

    @Autowired private WorkflowService workflowService;
    @Autowired private UserRepository userRepo;

    @PostMapping("/save")
    public ResponseEntity<WorkflowDTO> saveWorkflow(HttpServletRequest request, @RequestBody WorkflowDTO workflow) {
        Long userId = (Long) request.getAttribute("userId");
        Workflow savedWorkflow = workflowService.createOrUpdateWorkflow(userId, workflow);
        WorkflowDTO responseDTO = workflowService.convertToDTO(savedWorkflow);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping()
    public ResponseEntity<List<Map<String,Object>>> getAllWorkflows(HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(workflowService.getAllWorkflows(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkflowDTO> getWorkflow(HttpServletRequest request,@PathVariable("id") Long id){
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(workflowService.getWorkflow(userId,id));
    }

    @PatchMapping("/toggle/{id}")
    public ResponseEntity<Map<String, Object>> toggleWorkflow(HttpServletRequest request, @PathVariable("id") Long id) {
        Long userId = (Long) request.getAttribute("userId");
        Map<String, Object> response = new HashMap<>();

        try {
            Boolean success = workflowService.toggleWorkflow(id, userId);

            response.put("success", success);
            response.put("message", success ? "Workflow toggled successfully" : "Workflow is disabled");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "An error occurred while toggling the workflow");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}