package com.example.pdponline.payload.req;

import jakarta.validation.constraints.*;

public record SectionReq(

        @NotBlank(message = "Bo'lim sarlavhasi bo'sh bo'lmasligi kerak")
        @Size(min = 3, max = 100, message = "Sarlavha 3 dan 100 belgigacha bo'lishi kerak")
        String title,

        @NotNull(message = "Modul ID bo'sh bo'lmasligi kerak")
        @Positive(message = "Modul ID musbat son bo'lishi kerak")
        Long moduleId,

        boolean alreadyOpen

) {}
