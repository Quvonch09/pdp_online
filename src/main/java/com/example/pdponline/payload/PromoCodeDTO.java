package com.example.pdponline.payload;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PromoCodeDTO(String promoCode, String description, Double percentage, LocalDate expiryDate,boolean active) {
}
