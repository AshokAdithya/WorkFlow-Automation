// package com.pixels.zapierClone.automation_platform.service;

// import java.util.List;

// import org.springframework.stereotype.Service;

// import com.pixels.zapierClone.automation_platform.entity.Trigger;
// import com.pixels.zapierClone.automation_platform.entity.TriggerEventLog;
// import com.pixels.zapierClone.automation_platform.repository.TriggerEventLogRepository;

// @Service
// public class TriggerEventLogService {

//     private final TriggerEventLogRepository triggerEventLogRepository;

//     public TriggerEventLogService(TriggerEventLogRepository triggerEventLogRepository) {
//         this.triggerEventLogRepository = triggerEventLogRepository;
//     }

//     public List<TriggerEventLog> findByTrigger(Long triggerId) {
//         return triggerEventLogRepository.findByTriggerIdOrderByTriggeredAtDesc(triggerId);
//     }

//     public TriggerEventLog saveTriggerEventLog(TriggerEventLog log) {
//         return triggerEventLogRepository.save(log);
//     }
// }
