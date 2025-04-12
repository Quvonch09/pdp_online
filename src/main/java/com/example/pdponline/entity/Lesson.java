package com.example.pdponline.entity;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne
    private Section section;

    private boolean active;
}
