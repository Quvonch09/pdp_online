package com.example.pdponline.controller;

import com.example.pdponline.entity.User;
import com.example.pdponline.entity.enums.Role;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.auth.AuthLogin;
import com.example.pdponline.payload.auth.AuthRegister;
import com.example.pdponline.payload.auth.ResponseLogin;
import com.example.pdponline.security.CurrentUser;
import com.example.pdponline.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<ResponseLogin>> logIn(@Valid @RequestBody AuthLogin authLogin,
                                                            HttpServletRequest request) {
        return ResponseEntity.ok(authService.login(authLogin, request));
    }


    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @Operation(summary = "Super Admin user qushish uchun")
    @PostMapping("/saveUser")
    public ResponseEntity<ApiResponse<?>> adminSaveUser(@CurrentUser User user,
                                                        @RequestBody AuthRegister authRegister,
                                                        @RequestParam Role role) {
       return ResponseEntity.ok(authService.adminSaveUser(user,authRegister, role));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody AuthRegister authRegister,
                                                        @CurrentUser User user) {
        return ResponseEntity.ok(authService.register(user, authRegister));
    }

    @Operation(summary = "Parolni update qilish")
    @PutMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody AuthLogin authLogin) {
        return ResponseEntity.ok(authService.forgotPassword(authLogin));
    }
}
