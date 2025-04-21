package com.example.pdponline.controller;

import com.example.pdponline.entity.User;
import com.example.pdponline.entity.enums.PayType;
import com.example.pdponline.entity.enums.PaymentStatus;
import com.example.pdponline.payload.req.PaymentReq;
import com.example.pdponline.security.CurrentUser;
import com.example.pdponline.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService service;

    @PostMapping("/buy")
    @Operation(summary = "STUDENT Module sotib olish")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public ResponseEntity<?> buyModule(
            @RequestBody PaymentReq req,
            @RequestParam PayType type,
            @CurrentUser User user
    ){
        return ResponseEntity.ok(service.buyModule(user,req,type));
    }

    @PutMapping("/verify/{id}")
    @Operation(summary = "Payment ni tasdiqlash o'zgartirish")
    public ResponseEntity<?> changeStatus(
            @PathVariable Long id,
            @RequestParam PaymentStatus status
    ){
        return ResponseEntity.ok(service.verifyPayment(id, status));
    }

    @GetMapping("/get")
    @Operation(summary = "Paymentlarni filtrlab olish",description = "Student uchun paymentlar tarixi,Admin uchun paymentlarni hammasini ko'rish,filtrlash")
    public ResponseEntity<?> getPayments(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) PayType type,
            @RequestParam(required = false) PaymentStatus status,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Boolean promoCode,
            @RequestParam(required = false) Double startAmount,
            @RequestParam(required = false) Double endAmount,
            @RequestParam(required = false) List<Long> moduleIds
    ){
        return ResponseEntity.ok(service.getPayments(startDate, endDate, type, status, studentId, promoCode, startAmount, endAmount, moduleIds));
    }
}
