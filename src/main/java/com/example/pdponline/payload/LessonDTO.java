package com.example.pdponline.payload;

import lombok.Builder;
import lombok.Setter;

@Builder
public record LessonDTO(
        Long id,
        String name,
        String description,
        Long sectionId,
        boolean active,
        boolean isOpen,
        boolean isComplete
) {}
