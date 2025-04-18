package com.example.pdponline.entity;

import com.example.pdponline.entity.enums.FeedbackEnum;
import com.example.pdponline.entity.template.AbsEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Feedback extends AbsEntity {

    private String feedback;

    private int ball;

    @ManyToOne
    private User student;

    @Enumerated(EnumType.STRING)
    private FeedbackEnum feedbackEnum;
    @ManyToOne
    private Lesson lesson;
}
