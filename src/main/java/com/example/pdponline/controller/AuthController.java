package com.example.pdponline.controller;

import com.example.pdponline.payload.auth.AuthRegister;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.auth.AuthLogin;
import com.example.pdponline.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<ApiResponse> logIn(
            @Valid @RequestBody AuthLogin authLogin
    ){
        return ResponseEntity.ok(authService.login(authLogin));
    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody AuthRegister authRegister){
        return ResponseEntity.ok(authService.register(authRegister));
    }


    @Operation(summary = "Parolni update qilish")
    @PutMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody AuthLogin authLogin){
        ApiResponse apiResponse = authService.forgotPassword(authLogin);
        return ResponseEntity.ok(apiResponse);
    }

}
