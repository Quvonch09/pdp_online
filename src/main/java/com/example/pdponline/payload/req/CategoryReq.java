package com.example.pdponline.payload.req;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CategoryReq (
    @NotBlank(message = "Category nomi bo'sh bo'lmasin")
    String name,

    @NotBlank(message = "category description bo'sh bo'lmasin")
    String description,
    int duration,
    List<Long> mentorIds
){
}
