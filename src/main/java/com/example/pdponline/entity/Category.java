package com.example.pdponline.entity;

import com.example.pdponline.entity.enums.CategoryType;
import com.example.pdponline.entity.template.AbsEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Category extends AbsEntity {

    @Column(nullable = false)
    private String name;

    private String description;

    private int duration;

    @Column(nullable = false)
    private boolean active;

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;
}
