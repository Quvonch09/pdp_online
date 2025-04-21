package com.example.pdponline.controller;

import com.example.pdponline.entity.LessonTracking;
import com.example.pdponline.entity.User;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.LessonTrackingDTO;
import com.example.pdponline.security.CurrentUser;
import com.example.pdponline.service.LessonTrackingService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lessonTracking")
public class LessonTrackingController {
    private final LessonTrackingService lessonTrackingService;

    @PostMapping("/createLessonTracking")
    @Operation(summary = "Darsni tugatish")
    public ResponseEntity<ApiResponse<String>> createLessonTracking(@CurrentUser User user, @RequestBody LessonTrackingDTO lessonTrackingDTO) {
        return ResponseEntity.ok(lessonTrackingService.createLessonTracking(user,lessonTrackingDTO));
    }
}
