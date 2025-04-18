package com.example.pdponline.payload;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PaymentDTO(Long moduleId, Long userId, LocalDate payDate, double summa, String reason, String payType,
                         double chegirma) {
}
