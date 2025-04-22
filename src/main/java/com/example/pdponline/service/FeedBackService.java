package com.example.pdponline.service;

import com.example.pdponline.entity.Feedback;
import com.example.pdponline.entity.Lesson;
import com.example.pdponline.entity.User;
import com.example.pdponline.entity.enums.FeedbackEnum;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.FeedbackDTO;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.payload.res.ResPageable;
import com.example.pdponline.repository.FeedBackRepository;
import com.example.pdponline.repository.LessonRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedBackService {
    private final FeedBackRepository feedBackRepository;
    private final LessonRepository lessonRepository;

    public ApiResponse<String> saveFeedbackForLesson(User student, FeedbackDTO feedbackDTO) {
        Lesson lesson = lessonRepository.findById(feedbackDTO.getLessonId()).orElseThrow(
                () -> RestException.restThrow(ResponseError.NOTFOUND("Lesson"))
        );
        if (feedBackRepository.findByStudent_IdAndLesson_Id(student.getId(), lesson.getId()).isPresent()) {
            throw RestException.restThrow("Siz allaqachon feedback bildirgansiz");
        }
        Feedback build = Feedback.builder()
                .student(student)
                .lesson(lesson)
                .feedback(feedbackDTO.getFeedback())
                .feedbackEnum(FeedbackEnum.FOR_LESSON)
                .ball(feedbackDTO.getBall())
                .build();
        feedBackRepository.save(build);
        return ApiResponse.successResponse("feedback saved");
    }

    public ApiResponse<ResPageable> getFeedbackFromLesson(Long lessonId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Feedback> allByLessonId = feedBackRepository.getAllByLesson_Id(lessonId, pageable);
        return ApiResponse.successResponse(toResponseFeedback(page,size,allByLessonId));
    }

    private ResPageable toResponseFeedback(int page, int size, Page<Feedback> feedbacks) {
        List<FeedbackDTO> responseFeedbacks = new ArrayList<>();
        for (Feedback feedback : feedbacks) {
            responseFeedbacks.add(FeedbackDTO.builder()
                    .lessonId(feedback.getLesson().getId())
                    .id(feedback.getId())
                    .studentId(feedback.getStudent().getId())
                    .feedback(feedback.getFeedback())
                    .ball(feedback.getBall())
                    .build());
        }
        return ResPageable.builder()
                .page(page)
                .size(size)
                .totalElements(feedbacks.getTotalElements())
                .totalPage(feedbacks.getTotalPages())
                .body(responseFeedbacks)
                .build();
    }
}
