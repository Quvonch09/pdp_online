package com.example.pdponline.payload;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record ModuleDto (
        Long id,
        String name,
        Long courseId,
        double price,
        boolean active)
        implements Serializable
{
}
