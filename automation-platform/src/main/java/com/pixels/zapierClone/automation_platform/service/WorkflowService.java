// package com.pixels.zapierClone.automation_platform.service;

// import com.pixels.zapierClone.automation_platform.entity.User;
// import com.pixels.zapierClone.automation_platform.entity.Workflow;
// import com.pixels.zapierClone.automation_platform.repository.WorkflowRepository;
// import lombok.RequiredArgsConstructor;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.Optional;

// @Service
// @RequiredArgsConstructor
// public class WorkflowService {
    
//     @Autowired
//     private WorkflowRepository workflowRepository;

//     public Workflow createWorkflow(Workflow workflow) {
//         return workflowRepository.save(workflow);
//     }

//     public Optional<Workflow> getWorkflowById(Long id) {
//         return workflowRepository.findById(id);
//     }

//     public List<Workflow> getWorkflowsByUser(Long userId) {
//         return workflowRepository.findByUserId(userId);
//     }

//     public void deleteWorkflow(Long workflowId) {
//         workflowRepository.deleteById(workflowId);
//     }

//     public Workflow updateWorkflow(Workflow workflow) {
//         return workflowRepository.save(workflow);
//     }
// }

