package com.pixels.zapierClone.automation_platform.entity;

import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "triggers")
public class Trigger extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trigger_definition_id")
    private TriggerDefinition triggerDefinition;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String inputConfig;

    private String webhookPath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TriggerDefinition getTriggerDefinition() {
        return triggerDefinition;
    }

    public void setTriggerDefinition(TriggerDefinition triggerDefinition) {
        this.triggerDefinition = triggerDefinition;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getInputConfig() {
        return inputConfig;
    }

    public void setInputConfig(String inputConfig) {
        this.inputConfig = inputConfig;
    }

    public String getWebhookPath() {
        return webhookPath;
    }

    public void setWebhookPath(String webhookPath) {
        this.webhookPath = webhookPath;
    }

    // Getters and setters
    // ...
    
}
