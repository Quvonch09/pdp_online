package com.example.pdponline.mapper;

import com.example.pdponline.entity.Payment;
import com.example.pdponline.payload.ModuleDto;
import com.example.pdponline.payload.PaymentDTO;

import java.util.List;

public class PaymentMapper {

    public static PaymentDTO toDto(List<ModuleDto> moduleDtos,Payment payment){
        return PaymentDTO.builder()
                .id(payment.getId())
                .date(payment.getCreatedAt())
                .promoCode(payment.getPromoCode().getPromoCode())
                .type(payment.getPayType())
                .userId(payment.getStudent().getId())
                .status(payment.getStatus())
                .originalPrice(payment.getOriginalAmount())
                .paidPrice(payment.getPaidAmount())
                .modules(moduleDtos)
                .build();
    }
}
