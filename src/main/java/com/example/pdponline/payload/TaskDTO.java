package com.example.pdponline.payload;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record TaskDTO(
        Long id,
        String title,
        String description,
        Long lessonId,
        List<String> attachments,
        LocalDateTime starTime,
        LocalDateTime ednTime
) {
}
