package com.example.pdponline.controller;

import com.example.pdponline.payload.CourseBuyDTO;
import com.example.pdponline.service.CourseBuyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courseBuy")
@RequiredArgsConstructor

    private final CourseBuyService service;

    @GetMapping("/getByUserId")
    @Operation(summary = "UserId bo'yicha Talabani sotib olgan kurslarini ko'rsatadi!")
    public ResponseEntity<?> CourseBuy(@RequestParam Long userId) {
        return service.getByUserId(userId);
    }

    @PostMapping("/addCourseBuy")
    @Operation(summary = "Course sotib olish uchun!")
    public ResponseEntity<?> CourseBuy(@RequestBody CourseBuyDTO dto) {
        return service.AddCourseBuy(dto);
    }

}
