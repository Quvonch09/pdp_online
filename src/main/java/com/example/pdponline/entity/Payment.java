package com.example.pdponline.entity;

import com.example.pdponline.entity.enums.PayType;
import com.example.pdponline.entity.template.AbsEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Payment extends AbsEntity {

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PayType payType;

    @Column(nullable = false)
    @OneToMany
    private List<Course> courses;

    @Column(nullable = false)
    private Double summa;

    @Column(nullable = false)
    private String reason;

    private String promoCode;

    private Double chegirma;
}
