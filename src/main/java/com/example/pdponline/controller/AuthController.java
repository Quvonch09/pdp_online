package com.example.pdponline.controller;

import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.auth.AuthLogin;
import com.example.pdponline.payload.auth.AuthRegister;
import com.example.pdponline.payload.auth.ResponseLogin;
import com.example.pdponline.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<ResponseLogin>> logIn(@Valid @RequestBody AuthLogin authLogin, HttpServletRequest request) {
        return ResponseEntity.ok(authService.login(authLogin, request));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody AuthRegister authRegister) {
        return ResponseEntity.ok(authService.register(authRegister));
    }

    @Operation(summary = "Parolni update qilish")
    @PutMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody AuthLogin authLogin) {
        return ResponseEntity.ok(authService.forgotPassword(authLogin));
    }
}
