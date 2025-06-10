// package com.pixels.zapierClone.automation_platform.service;

// import com.pixels.zapierClone.automation_platform.entity.Action;
// import com.pixels.zapierClone.automation_platform.entity.Workflow;
// import com.pixels.zapierClone.automation_platform.repository.ActionRepository;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.Optional;

// @Service
// @RequiredArgsConstructor
// public class ActionService {

//     private final ActionRepository actionRepository;

//     public Action createAction(Action action) {
//         return actionRepository.save(action);
//     }

//     public Optional<Action> getActionById(Long id) {
//         return actionRepository.findById(id);
//     }

//     public List<Action> getActionsByWorkflow(Long workflowId) {
//         return actionRepository.findByWorkflowIdOrderByStepOrder(workflowId);
//     }

//     public Action updateAction(Action action) {
//         return actionRepository.save(action);
//     }

//     public void deleteAction(Long id) {
//         actionRepository.deleteById(id);
//     }
// }
