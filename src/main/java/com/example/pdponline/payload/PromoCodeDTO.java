package com.example.pdponline.payload;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PromoCodeDTO(String promoCode, String description, Integer percentage, LocalDate expiryDate,boolean active) {
}
