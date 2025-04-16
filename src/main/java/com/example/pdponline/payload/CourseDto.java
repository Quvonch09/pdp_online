package com.example.pdponline.payload;

import java.io.Serializable;
import java.time.LocalDateTime;

public record CourseDto(
    Long id,
    String title,
    Long categoryId,
    String categoryName,
    boolean active,
    LocalDateTime createdAt
)implements Serializable {
}
