package com.example.pdponline.payload;

import lombok.Builder;

@Builder
public record LessonDTO(
        Long id,
        String name,
        String description,
        Long sectionId,
        boolean active
) {}
