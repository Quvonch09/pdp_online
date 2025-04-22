package com.example.pdponline.payload;

import com.example.pdponline.entity.enums.PayType;
import com.example.pdponline.entity.enums.PaymentStatus;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PaymentDTO(
        Long id,
        LocalDateTime date,
        Long userId,
        PayType type,
        PaymentStatus status,
        Double originalPrice,
        Double paidPrice,
        String promoCode,
        List<ModuleDto> modules
) {
}
