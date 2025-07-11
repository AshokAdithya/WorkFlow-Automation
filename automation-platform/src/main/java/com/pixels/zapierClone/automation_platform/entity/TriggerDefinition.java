package com.pixels.zapierClone.automation_platform.entity;

import jakarta.persistence.*;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

@Entity
@Table(name = "trigger_definitions")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TriggerDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String triggerTypeIdentifier;

    private String name;

    private String description;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String configJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_integration_id")
    @JsonIgnore
    private AppIntegration appIntegration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTriggerTypeIdentifier() {
        return triggerTypeIdentifier;
    }

    public void setTriggerTypeIdentifier(String triggerTypeIdentifier) {
        this.triggerTypeIdentifier = triggerTypeIdentifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConfigJson() {
        return configJson;
    }

    public void setConfigJson(String configJson) {
        this.configJson = configJson;
    }

    public AppIntegration getAppIntegration() {
        return appIntegration;
    }

    public void setAppIntegration(AppIntegration appIntegration) {
        this.appIntegration = appIntegration;
    }

    // Getters and setters
    // ...
    

}
