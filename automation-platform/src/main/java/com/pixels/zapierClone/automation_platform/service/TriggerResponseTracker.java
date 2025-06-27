package com.pixels.zapierClone.automation_platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TriggerResponseTracker {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String PREFIX = "last_processed_time:";

    public Instant getLastProcessedTime(Long triggerId) {
        String val = redisTemplate.opsForValue().get(PREFIX + triggerId);
        return val != null ? Instant.parse(val) : Instant.EPOCH;
    }

    public void updateLastProcessedTime(Long triggerId, Instant time) {
        redisTemplate.opsForValue().set(PREFIX + triggerId, time.toString());
    }
}
