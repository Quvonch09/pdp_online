package com.example.pdponline.controller;

import com.example.pdponline.entity.User;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.req.PaymentReq;
import com.example.pdponline.security.CurrentUser;
import com.example.pdponline.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService service;

    @PostMapping("/pay")
    @Operation(summary = "module sotib olish!")
    public ResponseEntity<ApiResponse<?>> pay(@Valid @RequestBody PaymentReq req, @CurrentUser User user) {
        return ResponseEntity.ok(service.pay(req,user));
    }
}
