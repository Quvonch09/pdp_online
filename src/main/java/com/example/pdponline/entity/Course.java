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
public class Course extends AbsEntity {
    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Category category;

    @Column(nullable = false)
    private boolean active;
}
