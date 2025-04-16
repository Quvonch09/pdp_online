package com.example.pdponline.mapper;

import com.example.pdponline.entity.Section;
import com.example.pdponline.payload.SectionDto;

import java.util.List;

public class SectionMapper {

    public static SectionDto toDto(Section section) {
        return SectionDto.builder()
                .id(section.getId())
                .name(section.getTitle())
                .moduleId(section.getModule().getId())
                .active(section.isActive())
                .alreadyOpen(section.isAlreadyOpen())
                .build();
    }

    public static List<SectionDto> toDtoList(List<Section> sections) {
        return sections.stream()
                .map(SectionMapper::toDto)
                .toList();
    }
}
