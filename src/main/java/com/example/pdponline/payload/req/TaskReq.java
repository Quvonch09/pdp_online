package com.example.pdponline.payload.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class TaskReq {
    @NotBlank(message = "Task title bo'sh bo'lmasin")
    private String title;
    @NotBlank(message = "Task description bo'sh bo'lmasin")
    private String description;
    private List<String> filesUrl;

    @NotNull(message = "Lesson tanlanishi zarur")
    private Long lessonId;
    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
