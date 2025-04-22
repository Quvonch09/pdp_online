package com.example.pdponline.payload.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private LocalTime startTime;

    private LocalTime endTime;
}
