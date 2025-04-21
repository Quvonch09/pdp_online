package com.example.pdponline.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveToken(Long deviceId, String token, Duration ttl) {
        String key = "device:" + deviceId;
        redisTemplate.opsForValue().set(key, token, ttl);
    }

    public void removeToken(Long deviceId) {
        String key = "device:" + deviceId;
        redisTemplate.delete(key);
    }

    public boolean isTokenValid(Long deviceId, String token) {
        String key = "device:" + deviceId;
        String storedToken = redisTemplate.opsForValue().get(key);
        return token.equals(storedToken);
    }
}

