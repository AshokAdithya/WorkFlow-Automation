// package com.pixels.zapierClone.automation_platform.entity;

// import java.time.Instant;

// import jakarta.persistence.Entity;
// import jakarta.persistence.FetchType;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.Lob;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Table;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Entity
// @Data
// @Table(name="action_execution_logs")
// @AllArgsConstructor
// @NoArgsConstructor
// public class ActionExecutionLog {
//     @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "action_id", nullable = false)
//     private Action action;

//     private Instant executedAt;

//     private boolean success;

//     @Lob
//     private String responseJson;

//     private String errorMessage;
// }

package com.pixels.zapierClone.automation_platform.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "action_execution_logs")
public class ActionExecutionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "action_id")
    private Action action;

    private Instant executedAt;

    private boolean success;

    @Column(columnDefinition = "TEXT")
    private String responseJson;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Instant getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(Instant executedAt) {
        this.executedAt = executedAt;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResponseJson() {
        return responseJson;
    }

    public void setResponseJson(String responseJson) {
        this.responseJson = responseJson;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
