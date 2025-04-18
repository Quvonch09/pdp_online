package com.example.pdponline.entity;

import com.example.pdponline.entity.template.AbsEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class StudentValidModules extends AbsEntity {

    @NotNull(message = "module id null bo'lishi mumkin emas!")
    private Long moduleId;

    @NotNull(message = "student id null bo'lishi mumkin emas!")
    private Long studentId;

    private LocalDate startDate;

    private LocalDate endDate;

    @NotNull(message = "payment id null bo'lishi mumkin emas!")
    private Long paymentId;

    private boolean active;

}
