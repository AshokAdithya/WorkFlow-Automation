// package com.pixels.zapierClone.automation_platform.service;

// import com.pixels.zapierClone.automation_platform.entity.Trigger;
// import com.pixels.zapierClone.automation_platform.entity.Workflow;
// import com.pixels.zapierClone.automation_platform.repository.TriggerRepository;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.Optional;

// @Service
// @RequiredArgsConstructor
// public class TriggerService {

//     private final TriggerRepository triggerRepository;

//     public Trigger createTrigger(Trigger trigger) {
//         return triggerRepository.save(trigger);
//     }

//     public Optional<Trigger> getTriggerById(Long id) {
//         return triggerRepository.findById(id);
//     }

//     public List<Trigger> getTriggersByWorkflow(Long workflowId) {
//         return triggerRepository.findByWorkflowId(workflowId);
//     }

//     public Trigger updateTrigger(Trigger trigger) {
//         return triggerRepository.save(trigger);
//     }

//     public void deleteTrigger(Long id) {
//         triggerRepository.deleteById(id);
//     }
// }
