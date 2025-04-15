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
public class Section extends AbsEntity {

    @Column(nullable = false)
    private String title;

    @ManyToOne
    private Module module;

    private boolean alreadyOpen;

    private boolean active;
}
