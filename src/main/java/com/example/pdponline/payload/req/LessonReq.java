package com.example.pdponline.payload.req;

import com.example.pdponline.entity.Section;
import jakarta.validation.constraints.NotBlank;

public record LessonReq(
        @NotBlank(message = "Lesson name bo'sh bolmasligi kerak") String name,
        String description,
        Long sectionId
) {
}
