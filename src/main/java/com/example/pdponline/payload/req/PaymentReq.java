package com.example.pdponline.payload.req;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.util.List;

@Builder
public record PaymentReq(

        @NotNull(message = "Module IDlar bo'sh bo'lmasligi kerak")
        @Size(min = 1, message = "Kamida bitta modul tanlanishi kerak")
        List<@NotNull(message = "Har bir modul ID bo'sh bo'lmasligi kerak") Long> moduleIds,

        @Size(max = 10, message = "Promo kod 10 belgidan oshmasligi kerak")
        String promoCode

) {}
