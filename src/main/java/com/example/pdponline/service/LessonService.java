package com.example.pdponline.service;

import com.example.pdponline.entity.Lesson;
import com.example.pdponline.entity.Section;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.mapper.LessonMapper;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.LessonDTO;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.payload.req.LessonReq;
import com.example.pdponline.repository.LessonRepository;
import com.example.pdponline.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final SectionRepository sectionRepository;

    public ApiResponse<String> createLesson(LessonReq req) {
        Section section = findSectionByIdOrElseThrow(req.sectionId());
        Lesson lesson = Lesson.builder()
                .name(req.name())
                .description(req.description())
                .section(section)
                .active(true)
                .build();
        lessonRepository.save(lesson);
        return ApiResponse.successResponse("Successfully created lesson");
    }

    public ApiResponse<String> updateLesson(LessonReq req, Long id) {
        Lesson lesson = findLessonByIdOrElseThrow(id);
        Section section = findSectionByIdOrElseThrow(req.sectionId());
        lesson.setName(req.name());
        lesson.setDescription(req.description());
        lesson.setSection(section);
        lessonRepository.save(lesson);
        return ApiResponse.successResponse("Successfully updated lesson");
    }

    public ApiResponse<String> deleteLesson(Long lessonId) {
        Lesson lesson = findLessonByIdOrElseThrow(lessonId);
        lesson.setActive(false);
        lessonRepository.save(lesson);
        return ApiResponse.successResponse("Successfully deleted lesson");
    }

    public ApiResponse<List<LessonDTO>> getLessonsBySection(Long id) {
        List<Lesson> lessons = lessonRepository.findAllBySectionIdAndActiveIsTrue(id);
        if (lessons.isEmpty()) {
            throw RestException.restThrow(ResponseError.NOTFOUND("Lesson"));
        }
        return ApiResponse.successResponse(LessonMapper.toDoList(lessons));
    }

    public ApiResponse<LessonDTO> getLesson(Long id) {
        Lesson lesson = findLessonByIdOrElseThrow(id);

        return ApiResponse.successResponse(LessonMapper.toLessonDTO(lesson));
    }

    private Section findSectionByIdOrElseThrow(Long sectionId) {
        return sectionRepository.findById(sectionId)
                .orElseThrow(() ->
                        RestException.restThrow(ResponseError.NOTFOUND("Section")));
    }

    private Lesson findLessonByIdOrElseThrow(Long lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() ->
                        RestException.restThrow(ResponseError.NOTFOUND("Lesson")));
    }

}
