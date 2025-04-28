package com.example.pdponline.payload.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "Bo'sh bo'lmasin")
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Faqat soat va minut formatida (HH:mm) kiriting")
    private String startTime;

    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Faqat soat va minut formatida (HH:mm) kiriting")
    @NotBlank(message = "Bo'sh bo'lmasin")
    private String endTime;
}
