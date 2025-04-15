package com.example.pdponline.payload;

import lombok.Builder;

@Builder
public record ModuleDto (
        Long id,
        String name,
        Long courseId,
        double price,
        boolean active)
{
}
