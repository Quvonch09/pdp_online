package com.example.pdponline.service;

import com.example.pdponline.entity.Lesson;
import com.example.pdponline.entity.LessonTracking;
import com.example.pdponline.entity.Section;
import com.example.pdponline.entity.User;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.mapper.LessonMapper;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.LessonDTO;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.payload.req.LessonReq;
import com.example.pdponline.repository.LessonRepository;
import com.example.pdponline.repository.LessonTrackingRepository;
import com.example.pdponline.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final SectionRepository sectionRepository;
    private final LessonTrackingRepository lessonTrackingRepository;

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

    public ApiResponse<List<LessonDTO>> getLessonsBySection(User user, Long id) {
        List<Lesson> lessons = lessonRepository.findAllBySectionIdAndActiveIsTrue(id);
        if (lessons.isEmpty()) {
            throw RestException.restThrow(ResponseError.NOTFOUND("Bu sectionda lessonlar"));
        }

        return ApiResponse.successResponse(getOpenLesson(user, lessons));
    }

    private List<LessonDTO> getOpenLesson(User student, List<Lesson> lessons) {
        List<LessonDTO> lessonDTOS = new ArrayList<>();

        Set<Long> completedLessonIds = lessonTrackingRepository.findAllByStudentId(student.getId())
                .stream().filter(LessonTracking::isFinished)
                .map(tracking -> tracking.getLesson().getId())
                .collect(Collectors.toSet());

        boolean nextOpenAllowed = true;
        for (Lesson lesson : lessons) {
            boolean isCompleted = completedLessonIds.contains(lesson.getId());
            boolean isOpen = isCompleted || nextOpenAllowed;

            if (!isCompleted) {
                nextOpenAllowed = false;
            }
            lessonDTOS.add(LessonDTO.builder()
                            .id(lesson.getId())
                            .name(lesson.getName())
                            .description(lesson.getDescription())
                            .sectionId(lesson.getSection().getId())
                            .isOpen(isOpen)
                            .isComplete(isCompleted)
                            .active(lesson.isActive())
                    .build());

        }
        return lessonDTOS;
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
