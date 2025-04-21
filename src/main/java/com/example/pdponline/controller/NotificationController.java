package com.example.pdponline.controller;

import com.example.pdponline.entity.User;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.IdList;
import com.example.pdponline.payload.res.ResNotification;
import com.example.pdponline.security.CurrentUser;
import com.example.pdponline.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping
    @Operation(summary = "Admin barcha studentlarga notification yuborish")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<?>> sendNotification(@RequestBody ResNotification resNotification) {
        return ResponseEntity.ok(notificationService.adminSendNotificationAllStudents(resNotification));
    }


    @PostMapping("/{studentId}")
    @Operation(summary = "Admin bitta studentga notification yuborish")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<?>> sendNotificationOneStudent(@PathVariable Long studentId, @RequestBody ResNotification resNotification) {
        return ResponseEntity.ok(notificationService.sendNotification(studentId,null, resNotification));
    }



    @GetMapping("/my")
    @Operation(summary = "Barcha uziga kelgan notificationlarni kurish")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_TEACHER', 'ROLE_STUDENT', 'ROLE_TEACHER_ASSISTANT')")
    public ResponseEntity<ApiResponse<?>> getMyNotifications(@CurrentUser User student) {
        return ResponseEntity.ok(notificationService.getMyNotifications(student));
    }


    @PutMapping("/isRead")
    @Operation(summary = "O'qilmagan notificationlarni uqilgan qiladi")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_TEACHER', 'ROLE_STUDENT', 'ROLE_TEACHER_ASSISTANT')")
    public ResponseEntity<ApiResponse<?>> isRead(@RequestBody IdList idList) {
        return ResponseEntity.ok(notificationService.isReadNotification(idList));
    }


    @GetMapping("/count")
    @Operation(summary = "Barcha uziga kelgan uqilmagan notificationlar soni")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_TEACHER', 'ROLE_STUDENT', 'ROLE_TEACHER_ASSISTANT')")
    public ResponseEntity<ApiResponse<?>> getNotificationCount(@CurrentUser User student) {
        return ResponseEntity.ok(notificationService.getCountUnreadNotifications(student));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Admin notificationni uchirishi uchun")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<?>> deleteNotification(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.deleteNotification(id));
    }
}
