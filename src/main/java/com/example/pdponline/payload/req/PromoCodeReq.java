package com.example.pdponline.payload.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PromoCodeReq(
        @NotBlank(message = "description bo'sh bo'lishi mumkin emas!")
        String description,
        @NotNull(message = "percentage bo'sh bo'lishi mumkin emas!")
        @Min(value = 0, message = "percentage 1 dan kichik bo'lishi mumkin emas!")
        @Max(value = 100, message = "percentage 100 dan katta bo'lishi mumkin emas!")
        Integer percentage,
        LocalDate expiryDate) {
}
