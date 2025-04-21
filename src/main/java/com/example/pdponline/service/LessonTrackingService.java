package com.example.pdponline.service;

import com.example.pdponline.entity.Lesson;
import com.example.pdponline.entity.User;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.LessonTrackingDTO;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.repository.LessonRepository;
import com.example.pdponline.repository.LessonTrackingRepository;
import com.example.pdponline.repository.TaskRepository;
import com.example.pdponline.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonTrackingService {
    private final LessonTrackingRepository lessonTrackingRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final TaskRepository taskRepository;

    public ApiResponse<String> createLessonTracking(User user, LessonTrackingDTO lessonTrackingDTO) {
        Lesson lesson = lessonRepository.findById(lessonTrackingDTO.lessonId()).orElseThrow(
                () -> RestException.restThrow(ResponseError.NOTFOUND("Lesson"))
        );

        return null;
    }
}
