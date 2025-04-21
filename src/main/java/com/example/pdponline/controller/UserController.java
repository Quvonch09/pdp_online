package com.example.pdponline.controller;

import com.example.pdponline.entity.User;
import com.example.pdponline.entity.enums.Role;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.UserDTO;
import com.example.pdponline.payload.res.ResUser;
import com.example.pdponline.security.CurrentUser;
import com.example.pdponline.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/getMe")
    @Operation(summary = "Uzini profilini kurish")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_TEACHER', 'ROLE_STUDENT', 'ROLE_TEACHER_ASSISTANT')")
    public ResponseEntity<?> getMe(@CurrentUser User user, HttpServletRequest request) {
        return ResponseEntity.ok(userService.getMe(request, user));
    }


    @PutMapping("/{id}")
    @Operation(summary = "Uzini profilini tahrirlash uchun")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_TEACHER', 'ROLE_STUDENT', 'ROLE_TEACHER_ASSISTANT')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ResUser resUser, HttpServletRequest request) {
        return ResponseEntity.ok(userService.updateUser(request,id, resUser));
    }


    @GetMapping("/search")
    @Operation(summary = "Userlarni search qilish")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<?>> search(@RequestParam(required = false) String keyword,
                                                 @RequestParam(required = false) String phoneNumber,
                                                 @RequestParam Role role,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.searchUsers(keyword, phoneNumber, role, page, size));
    }
}
