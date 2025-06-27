package com.pixels.zapierClone.automation_platform.dto;

import java.util.List;
import java.util.Map;

public class TriggerResult {
    private boolean triggered;
    private List<Map<String, String>> data;

    public TriggerResult() {
    }

    public TriggerResult(boolean triggered, List<Map<String, String>> data) {
        this.triggered = triggered;
        this.data = data;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }
}
