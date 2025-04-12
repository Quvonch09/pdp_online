package com.example.pdponline.exception;

public class JwtException extends RuntimeException{
    public JwtException(String message) {
        super(message);
    }
}
