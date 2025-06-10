// package com.pixels.zapierClone.automation_platform.service;

// import java.util.List;

// import org.springframework.stereotype.Service;

// import com.pixels.zapierClone.automation_platform.entity.Action;
// import com.pixels.zapierClone.automation_platform.entity.ActionExecutionLog;
// import com.pixels.zapierClone.automation_platform.repository.ActionExecutionLogRepository;

// @Service
// public class ActionExecutionLogService {

//     private final ActionExecutionLogRepository actionExecutionLogRepository;

//     public ActionExecutionLogService(ActionExecutionLogRepository actionExecutionLogRepository) {
//         this.actionExecutionLogRepository = actionExecutionLogRepository;
//     }

//     public List<ActionExecutionLog> findByAction(Long actionId) {
//         return actionExecutionLogRepository.findByActionIdOrderByExecutedAtDesc(actionId);
//     }

//     public ActionExecutionLog saveActionExecutionLog(ActionExecutionLog log) {
//         return actionExecutionLogRepository.save(log);
//     }
// }
