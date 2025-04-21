package com.example.pdponline.entity;

import com.example.pdponline.entity.template.AbsEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(
        name = "task_result",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"task_id", "student_id"})
        }
)

public class TaskResult extends AbsEntity {

    @ManyToOne
    private User student;

    @ManyToOne
    private Task task;

    private int ball;

    private boolean success;
}
