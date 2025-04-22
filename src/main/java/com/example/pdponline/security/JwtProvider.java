package com.example.pdponline.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.example.pdponline.exception.JwtException;

import java.util.Date;
import java.util.Map;

@Component
public class JwtProvider {

    @Value("${jwt.secretkey}")
    private String key;
    @Value("${jwt.token.ttl}")
    private Long ttl;


    public String generateToken(Map<String, Object> claims, String phoneNumber) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(phoneNumber)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ttl))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }


    public String getPhoneNumberFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            throw new JwtException("JWT token has expired: " + e.getMessage());
        } catch (SignatureException e) {
            throw new JwtException("Invalid JWT signature: " + e.getMessage());
        } catch (Exception e) {
            throw new JwtException("Invalid JWT token: " + e.getMessage());
        }
    }


    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new JwtException("JWT token has expired: " + e.getMessage());
        } catch (SignatureException e) {
            throw new JwtException("Invalid JWT signature: " + e.getMessage());
        } catch (Exception e) {
            throw new JwtException("Invalid JWT token: " + e.getMessage());
        }
    }


    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    public Long extractDeviceId(String token) {
        return extractAllClaims(token).get("deviceId", Long.class);
    }


    public String extractUserId(String token) {
        return extractAllClaims(token).getSubject();
    }
}