package com.example.pdponline.service;

import com.example.pdponline.entity.Lesson;
import com.example.pdponline.entity.LessonTracking;
import com.example.pdponline.entity.TaskResult;
import com.example.pdponline.entity.User;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.mapper.TaskResultMapper;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.LessonTrackingDTO;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.payload.TaskResultDTO;
import com.example.pdponline.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonTrackingService {
    private final LessonTrackingRepository lessonTrackingRepository;
    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final TaskResultRepository taskResultRepository;


    public ApiResponse<String> finishLesson(User user, Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() ->
                RestException.restThrow(ResponseError.NOTFOUND("Lesson")));

        List<TaskResult> taskResults = taskResultRepository.findAllByStudentIdAndLesson_Id(user.getId(), lesson.getId());

        int userScore = taskResults.stream().mapToInt(TaskResult::getBall).sum();
        int totalScore = taskResults.size() * 10;

        double percentage = (double) userScore / totalScore * 100;

        if (percentage < 70) {
            return ApiResponse.successResponse("Siz 70% dan kam bal topladingiz qaytadan tasklarni bajariong");
        }
        lessonTrackingRepository.save(LessonTracking.builder()
                .lesson(lesson)
                .student(user)
                .finished(true)
                .build());
        return ApiResponse.successResponse("Lesson finished");

    }
}
