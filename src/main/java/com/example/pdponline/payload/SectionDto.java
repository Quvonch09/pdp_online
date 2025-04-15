package com.example.pdponline.payload;

import lombok.Builder;

@Builder
public record SectionDto (
        Long id,
        String name,
        Long moduleId,
        boolean active,
        boolean alreadyOpen
){
}
