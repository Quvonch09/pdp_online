package com.example.pdponline.controller;

import com.example.pdponline.entity.User;
import com.example.pdponline.payload.req.ModuleReq;
import com.example.pdponline.security.CurrentUser;
import com.example.pdponline.service.ModuleService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/module")
public class ModuleController {

    private final ModuleService moduleService;

    @PostMapping("/create")
    @Operation(summary = "SUPER_ADMIN,TEACHER Module yaratish")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_TEACHER')")
    public ResponseEntity<?> createModule(
            @RequestBody @Valid ModuleReq req
    ){
        return ResponseEntity.ok(moduleService.createModule(req));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "SUPER_ADMIN,TEACHER Module o'zgartirish")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_TEACHER')")
    public ResponseEntity<?> updateModule(
            @PathVariable Long id,
            @RequestParam String name
    ){
        return ResponseEntity.ok(moduleService.updateModule(id, name));
    }

    @PutMapping("/change-status/{id}")
    @Operation(summary = "SUPER_ADMIN,TEACHER Module active/inactive qilish")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_TEACHER')")
    public ResponseEntity<?> changeActive(
            @PathVariable Long id,
            @RequestParam boolean active
    ){
        return ResponseEntity.ok(moduleService.changeActive(id, active));
    }

    @GetMapping("/course/{id}")
    @Operation(summary = "Kurs bo'yicha modullar")
    public ResponseEntity<?> getByCourse(
            @PathVariable Long id,
            @RequestParam boolean active
    ){
        return ResponseEntity.ok(moduleService.getByCourse(id,active));
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID bo'yicha modul")
    public ResponseEntity<?> getById(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(moduleService.getById(id));
    }

    @GetMapping("/my-modules")
    @Operation(summary = "STUDENT User modullarini olish")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public ResponseEntity<?> myModules(
            @CurrentUser User user
    ){
        return ResponseEntity.ok(moduleService.boughtModules(user));
    }
}
