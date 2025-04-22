package com.example.pdponline.payload.req;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record LessonReq(
        @NotBlank(message = "Lesson name bo'sh bolmasligi kerak") String name,
        String description,
        Long sectionId,
        List<String> imgUrls
) {
}
