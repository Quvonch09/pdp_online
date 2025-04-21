package com.example.pdponline.payload;

public record SendNachatChat(
        Long userId,
        String message,
        Long taskId
) {
}
