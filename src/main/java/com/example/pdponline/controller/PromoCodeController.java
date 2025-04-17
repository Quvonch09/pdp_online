package com.example.pdponline.controller;

import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.PromoCodeDTO;
import com.example.pdponline.payload.req.PromoCodeReq;
import com.example.pdponline.service.PromoCodeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promoCode")
@RequiredArgsConstructor
public class PromoCodeController {

    private final PromoCodeService service;

    @PostMapping("/create")
    @Operation(summary = "Yangi PromoCode qo'shish uchun")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ApiResponse<?> createPromoCode(@Valid @RequestBody PromoCodeReq req) {
        return service.createPromoCode(req);
    }

    @GetMapping("/get/{promoCodeId}")
    @Operation(summary = "PromoCode Id bo'yicha DTO qaytaradi")
    public ResponseEntity<ApiResponse<PromoCodeDTO>> getPromoCode(@PathVariable Long promoCodeId) {
        return ResponseEntity.ok(service.getPromoCodeById(promoCodeId));
    }

    @GetMapping("/get/all")
    @Operation(summary = "Barcha PromoCodelarni qaytaradi!")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<List<PromoCodeDTO>>> getAllPromoCodes() {
        return ResponseEntity.ok(service.getAllPromoCodes());
    }

    @GetMapping("/get")
    @Operation(summary = "promoCode bo'yicha DTO qaytaradi!")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<PromoCodeDTO>> getPromoCodeByName(@RequestParam String promoCode) {
        return ResponseEntity.ok(service.getPromoCodeByName(promoCode));
    }

    @PutMapping("/update/{promoCodeId}")
    @Operation(summary = "update qilish uchun")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>> updatePromoCode(@PathVariable Long promoCodeId, @Valid @RequestBody PromoCodeReq req) {
        return ResponseEntity.ok(service.updatePromoCode(promoCodeId, req));
    }

    @DeleteMapping("/delete/{promoCodeIde}")
    @Operation(summary = "PromoCodeni Id bo'yicha o'chirish")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>> deletePromoCode(@PathVariable Long promoCodeIde) {
        return ResponseEntity.ok(service.deletePromoCode(promoCodeIde));
    }
}
