package com.pixels.zapierClone.automation_platform.service.utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderUtils {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{step(\\d+)\\.(.+?)}}");

    // For a single step output (like one GForms response)
    public static String resolvePlaceholders(String input, Map<String, Map<String, String>> stepSingleOutputs) {
        if (input == null) return null;

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(input);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            int stepIndex = Integer.parseInt(matcher.group(1));
            String key = matcher.group(2);
            String stepKey = "step" + stepIndex;

            Map<String, String> response = stepSingleOutputs.get(stepKey);
            String replacement = "";

            if (response != null) {
                replacement = response.getOrDefault(key, "");
            }

            matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(sb);
        return sb.toString();
    }

    // For a config map like { to: "...", subject: "...", body: "..." }
    public static Map<String, Object> resolveMapPlaceholders(
        Map<String, Object> inputConfig,
        Map<String, Map<String, String>> stepSingleOutputs
    ) {
        Map<String, Object> resolved = new HashMap<>();
        for (Map.Entry<String, Object> entry : inputConfig.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String) {
                resolved.put(entry.getKey(), resolvePlaceholders((String) value, stepSingleOutputs));
            } else {
                resolved.put(entry.getKey(), value);
            }
        }
        return resolved;
    }
}
