package com.example.pdponline.controller;

import com.example.pdponline.entity.enums.CategoryType;
import com.example.pdponline.payload.req.CategoryReq;
import com.example.pdponline.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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
            @RequestBody @Valid CategoryReq req
    ) {
        return ResponseEntity.ok(categoryService.addCategory(type, req));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "ADMIN Kategoriya o'zgartirish")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> updateCategory(
            @PathVariable Long id,
            @RequestBody @Valid CategoryReq req
    ) {
        return ResponseEntity.ok(categoryService.updateCategory(id, req));
    }

    @PutMapping("/change-status/{id}")
    @Operation(summary = "ADMIN Kategoriya active/inactive qilish")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> deleteCategory(
            @PathVariable Long id,
            @RequestParam boolean active
    ) {
        return ResponseEntity.ok(categoryService.changeActive(active, id));
    }

    @GetMapping("/search")
    @Operation(summary = "Categorylarni olish filtr asosida",description = "Barcha maydonlar null kelsa barcha categorylar qaytadi")
    public ResponseEntity<?> search(
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) CategoryType type
    ){
        return ResponseEntity.ok(categoryService.getCategories(active,type));
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID bo'yicha kategoriya")
    public ResponseEntity<?> getById(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(categoryService.getCategory(id));
    }
}
