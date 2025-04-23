package com.example.pdponline.service;

import com.example.pdponline.exception.JwtException;
import com.example.pdponline.repository.DeviceInfoRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final DeviceInfoRepository deviceInfoRepository;

    public void saveToken(Long deviceId, String token, Duration ttl) {
        String key = "device:" + deviceId;
        redisTemplate.opsForValue().set(key, token, ttl);
    }

    public void removeToken(Long deviceId) {
        String key = "device:" + deviceId;
        redisTemplate.delete(key);
    }

    public boolean isTokenValid(Long deviceId, String token) {
        try {
            String key = "device:" + deviceId;
            String storedToken = redisTemplate.opsForValue().get(key);
            return token.equals(storedToken);
        } catch (ExpiredJwtException e) {
            deviceInfoRepository.deleteById(deviceId);
            throw new JwtException("JWT token has expired: " + e.getMessage());
        } catch (SignatureException e) {
            throw new JwtException("Invalid JWT signature: " + e.getMessage());
        } catch (Exception e) {
            throw new JwtException("Invalid JWT token: " + e.getMessage());
        }
    }
}

