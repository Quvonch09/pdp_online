package com.example.pdponline.payload;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackDTO {
    @Schema(hidden = true)
    private Long id;

    @Schema(hidden = true)
    private Long studentId;

    private String feedback;

    @Min(value = 0,message = "0 dan kichkina bo'lmasligi kerak")
    @Max(value = 5,message = "5 dan kichkina bo'lmasligi kerak")
    private int ball;

    private Long lessonId;
}
