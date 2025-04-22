package com.example.pdponline.mapper;

import com.example.pdponline.entity.Lesson;
import com.example.pdponline.payload.LessonDTO;

import java.util.List;

public class LessonMapper {

    public static LessonDTO toLessonDTO(Lesson lesson, List<String> imgUrls) {
        return LessonDTO.builder()
                .id(lesson.getId())
                .name(lesson.getName())
                .description(lesson.getDescription())
                .imgUrls(lesson.getImgUrls())
                .active(lesson.isActive())
                .sectionId(lesson.getSection().getId())
                .build();
    }
    public static List<LessonDTO> toDoList(List<Lesson> lessons, List<String> imgUrls) {
        return lessons.stream().map(lesson -> toLessonDTO(lesson, imgUrls)).toList();
    }
}
