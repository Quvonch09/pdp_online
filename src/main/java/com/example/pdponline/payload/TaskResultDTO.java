package com.example.pdponline.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record TaskResultDTO(
        @Schema(hidden = true)
        Long id,
        @NotNull(message = "Student id bo'lishi kerak")
        Long studentId,
        @NotNull(message = "Task id bo'lishi kerak")
        Long TaskId,
        @Min(value = 0, message = "ball 0 dan kichkina bo'lmasiligi kerak")
        @Max(value = 10, message = "ball 10 dan kattta bo'lmasligi kerak")
        int ball
) {
}
