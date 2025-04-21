package com.example.pdponline.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record LessonTrackingDTO(
        @NotNull(message = "Lesson id bo'lishi kerak")
        Long lessonId,
        @Schema(hidden = true)
        boolean finished
) {
}
