package com.example.pdponline.service;

import com.example.pdponline.entity.Lesson;
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

    public ApiResponse<?> createLessonTracking(User user, LessonTrackingDTO lessonTrackingDTO) {
        Lesson lesson = lessonRepository.findById(lessonTrackingDTO.lessonId()).orElseThrow(
                () -> RestException.restThrow(ResponseError.NOTFOUND("Lesson"))
        );
        List<TaskResultDTO> taskResultDTOS=new ArrayList<>();
        for (TaskResult taskResult : taskResultRepository.findByStudentIdAndLesson_Id(user.getId(), lesson.getId())) {
         taskResultDTOS.add(TaskResultMapper.toTaskResultDTO(taskResult));
        }

        return ApiResponse.successResponse(taskResultDTOS);
    }
}
