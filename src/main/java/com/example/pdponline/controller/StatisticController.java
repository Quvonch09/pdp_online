package com.example.pdponline.controller;

import com.example.pdponline.service.StatisticService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistic")
public class StatisticController {

    private final StatisticService service;

    @GetMapping("/ceo")
    @Operation(summary = "SUPER_ADMIN uchun statistika")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<?> ceoDashboard(){
        return ResponseEntity.ok(service.ceoDashboard());
    }
}
