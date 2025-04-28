package com.example.pdponline.payload.req;

import jakarta.validation.constraints.*;

public record ModuleReq(

        @NotBlank(message = "Modul nomi bo'sh bo'lmasligi kerak")
        @Size(min = 3, max = 100, message = "Modul nomi 3 dan 100 belgigacha bo'lishi kerak")
        String name,

        @NotNull(message = "Kurs ID bo'sh bo'lmasligi kerak")
        @Positive(message = "Kurs ID musbat son bo'lishi kerak")
        Long courseId,

        @DecimalMin(value = "0.0", inclusive = true, message = "Narx manfiy bo'lishi mumkin emas")
        double price

) {}
