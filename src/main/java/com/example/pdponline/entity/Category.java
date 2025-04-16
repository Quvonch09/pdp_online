package com.example.pdponline.entity;

import com.example.pdponline.entity.enums.CategoryType;
import com.example.pdponline.entity.template.AbsEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> mentors;
}
