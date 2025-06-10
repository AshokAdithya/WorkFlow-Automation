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
// @Table(name = "trigger_event_logs")
// @AllArgsConstructor
// @NoArgsConstructor
// public class TriggerEventLog {
//     @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "trigger_id", nullable = false)
//     private Trigger trigger;

//     private Instant triggeredAt;

//     @Lob
//     private String payloadJson;
// }

package com.pixels.zapierClone.automation_platform.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "trigger_event_logs")
public class TriggerEventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trigger_id")
    private Trigger trigger;

    private Instant triggeredAt;

    @Column(columnDefinition = "jsonb")
    private String payloadJson;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    public Instant getTriggeredAt() {
        return triggeredAt;
    }

    public void setTriggeredAt(Instant triggeredAt) {
        this.triggeredAt = triggeredAt;
    }

    public String getPayloadJson() {
        return payloadJson;
    }

    public void setPayloadJson(String payloadJson) {
        this.payloadJson = payloadJson;
    }
}

