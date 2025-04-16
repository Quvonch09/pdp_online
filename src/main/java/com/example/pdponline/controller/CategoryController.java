package com.example.pdponline.controller;

import com.example.pdponline.entity.enums.CategoryType;
import com.example.pdponline.payload.req.CategoryReq;
import com.example.pdponline.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/add")
    @Operation(summary = "ADMIN Kategoriya qo'shish")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> addCategory(
            @RequestParam CategoryType type,
            @RequestBody CategoryReq req
    ) {
        return ResponseEntity.ok(categoryService.addCategory(type, req));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "ADMIN Kategoriya o'zgartirish")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryReq req
    ) {
        return ResponseEntity.ok(categoryService.updateCategory(id, req));
    }

    @DeleteMapping("/change-status/{id}")
    @Operation(summary = "ADMIN Kategoriya active/inactive qilish")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> deleteCategory(
            @PathVariable Long id,
            @RequestParam boolean active
    ) {
        return ResponseEntity.ok(categoryService.changeActive(active, id));
    }

    @GetMapping("/all")
    @Operation(summary = "Barcha kategoriyalar")
    public ResponseEntity<?> getAllCategory() {
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    @GetMapping("/active")
    @Operation(summary = "ADMIN Active bo'yicha kategoriyalar")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> getActiveCategory(
            @RequestParam boolean active
    ) {
        return ResponseEntity.ok(categoryService.getByActive(active));
    }

    @GetMapping("/type")
    @Operation(summary = "Kategoriya turi bo'yicha kategoriyalar")
    public ResponseEntity<?> getByType(
            @RequestParam CategoryType type
    ){
        return ResponseEntity.ok(categoryService.getByType(type));
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID bo'yicha kategoriya")
    public ResponseEntity<?> getById(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(categoryService.getCategory(id));
    }
}
