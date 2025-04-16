package com.example.pdponline.mapper;

import com.example.pdponline.entity.Lesson;
import com.example.pdponline.payload.LessonDTO;

import java.util.List;

public class LessonMapper {

    public static LessonDTO toLessonDTO(Lesson lesson) {
        return LessonDTO.builder()
                .id(lesson.getId())
                .name(lesson.getName())
                .description(lesson.getDescription())
                .active(lesson.isActive())
                .sectionId(lesson.getSection().getId())
                .build();
    }
    public static List<LessonDTO> toDoList(List<Lesson> lessons) {
        return lessons.stream().map(LessonMapper::toLessonDTO).toList();
    }
}
