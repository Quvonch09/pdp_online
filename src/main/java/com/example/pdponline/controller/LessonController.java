package com.example.pdponline.controller;

import com.example.pdponline.entity.Lesson;
import com.example.pdponline.entity.User;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.LessonDTO;
import com.example.pdponline.payload.req.LessonReq;
import com.example.pdponline.security.CurrentUser;
import com.example.pdponline.service.LessonService;
import com.example.pdponline.service.LessonTrackingService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lesson")
public class LessonController {
    private final LessonService lessonService;
    private final LessonTrackingService lessonTrackingService;

    @PostMapping("/create")
    @Operation(summary = "SUPER_ADMIN,TEACHER Lesson yaratish")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_TEACHER')")
    public ResponseEntity<ApiResponse<String>> createLesson(@Valid @RequestBody LessonReq req) {
        return ResponseEntity.ok(lessonService.createLesson(req));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "SUPER_ADMIN,TEACHER Lesson update")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_TEACHER')")
    public ResponseEntity<ApiResponse<String>> updateLesson(@Valid @RequestBody LessonReq req, @PathVariable Long id) {
        return ResponseEntity.ok(lessonService.updateLesson(req, id));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "SUPER_ADMIN,TEACHER Lesson delete")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_TEACHER')")
    public ResponseEntity<ApiResponse<String>> deleteLesson(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.deleteLesson(id));
    }

    @GetMapping("/section/{id}")
    @Operation(summary = "Section orqali  lessonlarni olish")
    public ResponseEntity<ApiResponse<List<LessonDTO>>> getLessons(@CurrentUser User user, @PathVariable Long id) {
        return ResponseEntity.ok(lessonService.getLessonsBySection(user,id));
    }

    @GetMapping("/{id}")
    @Operation(summary = "id orqali lessonni olish")
    public ResponseEntity<ApiResponse<LessonDTO>> getLesson(@PathVariable Long id) {
        return ResponseEntity.ok(lessonService.getLesson(id));
    }

    @PostMapping("/{moduleId}/{lessonId}/finished")
    @Operation()
    public ResponseEntity<ApiResponse<String>> finishLesson(@CurrentUser User user, @PathVariable Long lessonId, @PathVariable Long moduleId) {
        return ResponseEntity.ok(lessonTrackingService.finishLesson(user,lessonId,moduleId));
    }
}
