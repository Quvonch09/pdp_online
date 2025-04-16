package com.example.pdponline.payload;

import com.example.pdponline.entity.enums.CategoryType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record CategoryDto (
        Long id,
        String name,
        String description,
        int duration,
        boolean active,
        CategoryType categoryType,
        LocalDateTime createdAt,
        List<MentorDto> mentors
) implements Serializable {
}
