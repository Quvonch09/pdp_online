package com.example.pdponline.entity;


import com.example.pdponline.entity.enums.CourseBuyType;
import com.example.pdponline.entity.enums.PayType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CourseBuy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long userId;
    @NotNull
    @OneToMany
    private List<Course> courseList;
    private Double chegirma;
    private Long promoCode;
    @NotNull
    private Double allPrice;
    @NotNull
    @Enumerated(EnumType.STRING)
    private CourseBuyType courseType;
    @NotNull
    @Enumerated(EnumType.STRING)
    private PayType payType;
}
