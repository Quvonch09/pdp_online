package com.example.pdponline.entity;

import com.example.pdponline.entity.enums.PayType;
import com.example.pdponline.entity.template.AbsEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Payment extends AbsEntity {

    @Column(nullable = false)
    private Long userId;
//
//    @JoinColumn(nullable = false)
//    @ManyToOne
//    private Transaction transactionId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PayType payType;

    @Column(nullable = false)
    private Long moduleId;

    @Column(nullable = false)
    private Double summa;

    @Column(nullable = false)
    private String reason;

    @ManyToOne
    private PromoCode promoCode;
}
