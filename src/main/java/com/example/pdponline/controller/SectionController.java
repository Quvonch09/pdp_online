package com.example.pdponline.controller;

import com.example.pdponline.payload.req.SectionReq;
import com.example.pdponline.service.SectionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/section")
public class SectionController {

    private final SectionService service;

    @PostMapping("/create")
    @Operation(summary = "SUPER_ADMIN,TEACHER Section yaratish")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_TEACHER')")
    public ResponseEntity<?> createSection(
            @RequestBody SectionReq req
    ){
        return ResponseEntity.ok(service.createSection(req));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "SUPER_ADMIN,TEACHER Section ni yangilash")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_TEACHER')")
    public ResponseEntity<?> updateSection(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam boolean alreadyOpen
    ){
        return ResponseEntity.ok(service.update(id, title, alreadyOpen));
    }

    @PutMapping("/active/{id}")
    @Operation(summary = "SUPER_ADMIN,TEACHER Section active sini o'zgartirish")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_TEACHER')")
    public ResponseEntity<?> changeActive(
            @PathVariable Long id,
            @RequestParam boolean active
    ){
        return ResponseEntity.ok(service.changeActive(id, active));
    }

    @GetMapping("/module/{id}")
    @Operation(summary = "Module bo'yicha sectionlarni olish")
    public ResponseEntity<?> getByModule(
            @PathVariable Long id,
            @RequestParam boolean active
    ){
        return ResponseEntity.ok(service.getByModule(id, active));
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID bo'yicha section ni olish")
    public ResponseEntity<?> getById(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(service.getById(id));
    }
}
