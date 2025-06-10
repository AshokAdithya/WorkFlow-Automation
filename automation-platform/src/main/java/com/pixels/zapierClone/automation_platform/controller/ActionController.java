// package com.pixels.zapierClone.automation_platform.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.pixels.zapierClone.automation_platform.entity.Action;
// import com.pixels.zapierClone.automation_platform.repository.ActionRepository;

// @RestController
// @RequestMapping("/api/actions")
// public class ActionController {

//     @Autowired private ActionRepository actionRepository;

//     @PostMapping
//     public ResponseEntity<Action> create(@RequestBody Action action) {
//         return ResponseEntity.ok(actionRepository.save(action));
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<Action> get(@PathVariable Long id) {
//         return actionRepository.findById(id)
//                 .map(ResponseEntity::ok)
//                 .orElse(ResponseEntity.notFound().build());
//     }
// }

package com.pixels.zapierClone.automation_platform.controller;

import com.pixels.zapierClone.automation_platform.entity.Action;
import com.pixels.zapierClone.automation_platform.repository.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/actions")
public class ActionController {

    @Autowired private ActionRepository actionRepository;

    @PostMapping
    public ResponseEntity<Action> createAction(@RequestBody Action action) {
        return ResponseEntity.ok(actionRepository.save(action));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Action> updateAction(@PathVariable Long id, @RequestBody Action updatedAction) {
        return actionRepository.findById(id)
                .map(existingAction -> {
                    existingAction.setActionTypeIdentifier(updatedAction.getActionTypeIdentifier());
                    existingAction.setConfigJson(updatedAction.getConfigJson());
                    existingAction.setName(updatedAction.getName());
                    existingAction.setDescription(updatedAction.getDescription());
                    existingAction.setStepOrder(updatedAction.getStepOrder());
                    return ResponseEntity.ok(actionRepository.save(existingAction));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Action> getActionById(@PathVariable Long id) {
        return actionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAction(@PathVariable Long id) {
        actionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}