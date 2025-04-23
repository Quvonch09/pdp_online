package com.example.pdponline.payload.req;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PromoCodeReq(

        @NotBlank(message = "Tavsif (description) bo'sh bo'lishi mumkin emas!")
        @Size(max = 255, message = "Tavsif 255 belgidan oshmasligi kerak")
        String description,

        @NotNull(message = "Foiz qiymati (percentage) bo'sh bo'lishi mumkin emas!")
        @Min(value = 1, message = "Foiz 1 dan kichik bo'lishi mumkin emas!")
        @Max(value = 100, message = "Foiz 100 dan katta bo'lishi mumkin emas!")
        Integer percentage,

        @NotNull(message = "Amal qilish muddati (expiryDate) bo'sh bo'lmasligi kerak")
        @FutureOrPresent(message = "Amal qilish muddati hozirgi kundan oldin bo'lishi mumkin emas")
        LocalDate expiryDate

) {}
