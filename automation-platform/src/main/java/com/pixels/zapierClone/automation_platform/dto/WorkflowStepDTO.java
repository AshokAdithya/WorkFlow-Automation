package com.pixels.zapierClone.automation_platform.dto;

import java.util.Map;

public class WorkflowStepDTO {
    private int stepOrder;
    private String type;
    private Long app;    
    private Long event;   
    private String inputConfig;
    private String webhookUrl; 

    public int getStepOrder() {
        return stepOrder;
    }

    public void setStepOrder(int stepOrder) {
        this.stepOrder = stepOrder;
    }

    public Long getApp() {
        return app;
    }

    public void setApp(Long app) {
        this.app = app;
    }

    public Long getEvent() {
        return event;
    }

    public void setEvent(Long event) {
        this.event = event;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInputConfig() {
        return inputConfig;
    }

    public void setInputConfig(String config) {
        this.inputConfig = config;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }
}
