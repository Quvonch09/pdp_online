package com.example.pdponline.mapper;

import com.example.pdponline.entity.Category;
import com.example.pdponline.payload.CategoryDto;
import com.example.pdponline.payload.MentorDto;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {

    // 1. Entity -> DTO
    public static CategoryDto toDto(List<MentorDto> mentors, Category category) {
        if (category == null) return null;
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getDuration(),
                category.isActive(),
                category.getCategoryType(),
                category.getCreatedAt(),
                mentors
        );
    }

    // 2. DTO -> Entity
    public static Category toEntity(CategoryDto dto) {
        if (dto == null) return null;
        Category category = new Category();
        category.setId(dto.id()); // agar ID ni qo‘lda o‘rnatish kerak bo‘lsa
        category.setName(dto.name());
        category.setDescription(dto.description());
        category.setDuration(dto.duration());
        category.setActive(dto.active());
        category.setCategoryType(dto.categoryType());
        category.setCreatedAt(dto.createdAt());
        return category;
    }

    // 3. Entity list -> DTO list (mentors = null)
    public static List<CategoryDto> toDtoList(List<MentorDto> mentors,List<Category> categories) {
        return categories.stream()
                .map(category -> toDto(mentors, category))
                .collect(Collectors.toList());
    }

    // 4. DTO list -> Entity list
    public static List<Category> toEntityList(List<CategoryDto> dtos) {
        return dtos.stream()
                .map(CategoryMapper::toEntity)
                .collect(Collectors.toList());
    }
}
