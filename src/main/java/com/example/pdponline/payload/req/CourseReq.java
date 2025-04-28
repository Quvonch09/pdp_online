package com.example.pdponline.payload.req;

import jakarta.validation.constraints.*;

public record CourseReq(

        @NotBlank(message = "Kurs nomi bo'sh bo'lmasligi kerak")
        @Size(min = 3, max = 100, message = "Kurs nomi 3 dan 100 belgigacha bo'lishi kerak")
        String name,

        @NotNull(message = "Category ID bo'sh bo'lmasligi kerak")
        @Positive(message = "Category ID musbat son bo'lishi kerak")
        Long categoryId

) {}
