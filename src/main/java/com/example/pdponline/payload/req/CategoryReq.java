package com.example.pdponline.payload.req;

import jakarta.validation.constraints.*;

import java.util.List;

public record CategoryReq(

        @NotBlank(message = "Category nomi bo'sh bo'lmasin")
        @Size(min = 3, max = 100, message = "Category nomi 3 dan 100 gacha bo'lishi kerak")
        String name,

        @NotBlank(message = "Category description bo'sh bo'lmasin")
        @Size(max = 500, message = "Category description 500 belgidan oshmasligi kerak")
        String description,

        @NotNull(message = "Davomiylik bo'sh bo'lmasin")
        @Min(value = 1, message = "Davomiylik kamida 1 bo'lishi kerak")
        @Max(value = 1000, message = "Davomiylik 1000 dan oshmasligi kerak")
        Integer duration,

        @NotNull(message = "Mentor IDlar bo'sh bo'lmasligi kerak")
        @Size(min = 1, message = "Kamida bitta mentor tanlanishi kerak")
        List<@NotNull(message = "Har bir mentor ID bo'sh bo'lmasligi kerak") Long> mentorIds
) {}
