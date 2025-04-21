package com.example.pdponline.controller;

import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.req.PromoCodeReq;
import com.example.pdponline.service.PromoCodeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/promoCode")
@RequiredArgsConstructor
public class PromoCodeController {

    private final PromoCodeService service;

    @PostMapping("/create")
    @Operation(summary = "[SUPER_ADMIN] Yangi PromoCode qo'shish uchun")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ApiResponse<?> createPromoCode(@Valid @RequestBody PromoCodeReq req) {
        return service.createPromoCode(req);
    }

    @GetMapping("/get")
    @Operation(summary = "SUPER_ADMIN,TEACHER promo kodlarni ko'rish",description = "Kiritilgan field bo'yicha search bo'ladi, Agar hammasi bo'sh kiritilsa barcha promokodlar qaytadi")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_TEACHER')")
    public ResponseEntity<?> getPromoCodes(
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String code,
            @RequestParam(required = false)LocalDate expiryDate
    ){
        return ResponseEntity.ok(service.getPromoCodes(active, code, expiryDate));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "SUPER_ADMIN,TEACHER promokod ni o'chirish")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_TEACHER')")
    public ResponseEntity<?> deletePromoCode(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(service.unActive(id));
    }
 }
