package com.pixels.zapierClone.automation_platform.dto;

import java.util.List;

import com.pixels.zapierClone.automation_platform.entity.ActionDefinition;
import com.pixels.zapierClone.automation_platform.entity.TriggerDefinition;

public class AppIntegrationDTO {
    private Long id;
    private String name;
    private String identifier;
    private String description;
    private String logoUrl;
    private String authType;
    private boolean connected;
    private List<ActionDefinition> actionDefinitions;
    private List<TriggerDefinition> triggerDefinitions;

    public AppIntegrationDTO(Long id, String name, String identifier, String description,
                             String logoUrl, String authType, boolean connected,List<ActionDefinition> actionDefinitions,List<TriggerDefinition> triggerDefinitions) {
        this.id = id;
        this.name = name;
        this.identifier = identifier;
        this.description = description;
        this.logoUrl = logoUrl;
        this.authType = authType;
        this.connected = connected;
        this.actionDefinitions = actionDefinitions;
        this.triggerDefinitions = triggerDefinitions;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getIdentifier() { return identifier; }
    public String getDescription() { return description; }
    public String getLogoUrl() { return logoUrl; }
    public String getAuthType() { return authType; }
    public boolean isConnected() { return connected; }

    public List<ActionDefinition> getActionDefinitions() {
        return actionDefinitions;
    }

    public List<TriggerDefinition> getTriggerDefinitions() {
        return triggerDefinitions;
    }
}
