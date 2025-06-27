package com.pixels.zapierClone.automation_platform.entity;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;

@Entity
@Table(name = "app_integrations")
public class AppIntegration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String identifier;

    private String description;

    private String logoUrl;

    private String authType;

    @OneToMany(mappedBy = "appIntegration", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TriggerDefinition> triggerDefinitions;

    @OneToMany(mappedBy = "appIntegration", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ActionDefinition> actionDefinitions;

        public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public List<TriggerDefinition> getTriggerDefinitions() {
        return triggerDefinitions;
    }

    public void setTriggerDefinitions(List<TriggerDefinition> triggerDefinitions) {
        this.triggerDefinitions = triggerDefinitions;
    }

    public List<ActionDefinition> getActionDefinitions() {
        return actionDefinitions;
    }

    public void setActionDefinitions(List<ActionDefinition> actionDefinitions) {
        this.actionDefinitions = actionDefinitions;
    }

}
