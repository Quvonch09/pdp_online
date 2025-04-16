package com.example.pdponline.payload;

import com.example.pdponline.entity.Course;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseBuyDTO {
    private Long userId;
    private List<Integer> courseIdList;
    private String payType;
    private Double chegirma;
    private Double promoCode;
}
