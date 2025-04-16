package com.example.pdponline.payload;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record SectionDto (
        Long id,
        String name,
        Long moduleId,
        boolean active,
        boolean alreadyOpen
) implements Serializable {
}
