package com.example.pdponline.entity;

import com.example.pdponline.entity.template.AbsEntity;
import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Lesson extends AbsEntity {

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne
    private Section section;

    private boolean active;
}
