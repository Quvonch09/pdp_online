package com.example.pdponline.entity;

import com.example.pdponline.entity.enums.PayType;
import com.example.pdponline.entity.enums.PaymentStatus;
import com.example.pdponline.entity.template.AbsEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment extends AbsEntity {
    @ManyToOne
    private User student;

    @ManyToOne
    private PromoCode promoCode;

    @Column(nullable = false)
    private Double originalAmount;

    @Column(nullable = false)
    private Double paidAmount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    private PayType payType;

    @CreationTimestamp
    private LocalDate payDate;
}
