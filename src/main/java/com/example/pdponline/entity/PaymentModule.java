package com.example.pdponline.entity;

import com.example.pdponline.entity.template.AbsEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "payment_modules")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentModule extends AbsEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Payment payment;

    @ManyToOne(fetch = FetchType.EAGER)
    private Module module;

    @Column(nullable = false)
    private Double priceAtPurchase;

    @Column(nullable = false)
    private boolean active;
}
