package com.example.pdponline.entity;

import com.example.pdponline.entity.template.AbsEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Module extends AbsEntity {

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Course course;

    @Column(nullable = false)
    private double price;

    private boolean active;
}
