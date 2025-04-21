package com.example.pdponline.entity;

import com.example.pdponline.entity.template.AbsEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoCode extends AbsEntity {

    @Column(unique = true, nullable = false)
    private String promoCode;

    private String description;

    @Column(nullable = false)
    private Integer percentage;

    private boolean active;

    @Column(nullable = false)
    private LocalDate expiryDate;
}
