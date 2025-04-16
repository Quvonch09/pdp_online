package com.example.pdponline.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;


public record NotificationDTO (
        Long id,

        String title,

        String content,

        Long userId,

        boolean read,

        LocalDateTime createdAt
) {
}
