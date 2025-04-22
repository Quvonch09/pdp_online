package com.example.pdponline.payload;

import lombok.Builder;
import lombok.Setter;

import java.util.List;

@Builder
public record LessonDTO(
        Long id,
        String name,
        String description,
        List<String> imgUrls,
        Long sectionId,
        boolean active,
        boolean isOpen,
        boolean isComplete
) {}
