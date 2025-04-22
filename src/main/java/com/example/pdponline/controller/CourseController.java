package com.example.pdponline.controller;

import com.example.pdponline.payload.req.CourseReq;
import com.example.pdponline.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/create")
    @Operation(summary = "SUPER_ADMIN,TEACHER kurs yaratish")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_TEACHER')")
    public ResponseEntity<?> createCourse(
            @RequestBody CourseReq req
    ){
        return ResponseEntity.ok(courseService.createCourse(req));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "SUPER_ADMIN,TEACHER kursni o'zgartirish")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_TEACHER')")
    public ResponseEntity<?> updateCourse(
            @PathVariable Long id,
            @RequestParam String name
    ) {
        return ResponseEntity.ok(courseService.updateCourse(id, name));
    }

    @PutMapping("/change-status/{id}")
    @Operation(summary = "SUPER_ADMIN,TEACHER kursni active/inactive qilish")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_TEACHER')")
    public ResponseEntity<?> changeActive(
            @PathVariable Long id,
            @RequestParam boolean active
    ) {
        return ResponseEntity.ok(courseService.changeActive(id, active));
    }

    @GetMapping("/category/{id}")
    @Operation(summary = "Kategoriya bo'yicha kurslar")
    public ResponseEntity<?> getByCategory(
            @PathVariable Long id,
            @RequestParam boolean active
    ){
        return ResponseEntity.ok(courseService.getByCategory(id, active));
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID bo'yicha kurs")
    public ResponseEntity<?> getCourse(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(courseService.getCourse(id));
    }
}

