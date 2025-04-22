package com.example.pdponline.controller;

import com.example.pdponline.entity.Feedback;
import com.example.pdponline.entity.User;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.FeedbackDTO;
import com.example.pdponline.payload.res.ResPageable;
import com.example.pdponline.security.CurrentUser;
import com.example.pdponline.service.FeedBackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/feedback")
@RequiredArgsConstructor
public class FeedBackController {
    private final FeedBackService feedBackService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<String>> saveFeedbackForLesson(
            @CurrentUser User user,
            @RequestBody FeedbackDTO feedbackDTO) {

        return ResponseEntity.ok(feedBackService.saveFeedbackForLesson(user,feedbackDTO));
    }

    @GetMapping("get/{lessonId}")
    public ResponseEntity<ApiResponse<ResPageable>> getFeedbackFromLesson(
            @PathVariable Long lessonId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return ResponseEntity.ok(feedBackService.getFeedbackFromLesson(lessonId,page,size));
    }
}
