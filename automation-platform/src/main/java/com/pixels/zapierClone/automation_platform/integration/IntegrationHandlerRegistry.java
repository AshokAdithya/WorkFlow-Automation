package com.pixels.zapierClone.automation_platform.integration;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class IntegrationHandlerRegistry {

    private final Map<String, IntegrationHandler> handlerMap = new HashMap<>();

    public IntegrationHandlerRegistry(List<IntegrationHandler> handlers) {
        for (IntegrationHandler handler : handlers) {
            handlerMap.put(handler.getServiceIdentifier(), handler);
        }
    }

    public IntegrationHandler getHandler(String serviceIdentifier) {
        IntegrationHandler handler = handlerMap.get(serviceIdentifier);
        if (handler == null) {
            throw new IllegalArgumentException("Unsupported service: " + serviceIdentifier);
        }
        return handler;
    }
}
