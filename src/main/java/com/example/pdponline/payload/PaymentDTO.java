package com.example.pdponline.payload;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentDTO {
    private List<Long> courseIdList;
    private String payType;
    private String promoCode;
}
