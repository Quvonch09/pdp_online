package com.example.pdponline.service;

import com.example.pdponline.exception.JwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
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
        try {
            String key = "device:" + deviceId;
            String storedToken = redisTemplate.opsForValue().get(key);
            return token.equals(storedToken);
        } catch (ExpiredJwtException e) {
            throw new JwtException("JWT token has expired: " + e.getMessage());
        } catch (SignatureException e) {
            throw new JwtException("Invalid JWT signature: " + e.getMessage());
        } catch (Exception e) {
            throw new JwtException("Invalid JWT token: " + e.getMessage());
        }
    }
}

