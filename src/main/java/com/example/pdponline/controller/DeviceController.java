package com.example.pdponline.controller;

import com.example.pdponline.entity.User;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.DeviceInfoDTO;
import com.example.pdponline.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<DeviceInfoDTO>>> getMyDevices(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(deviceService.getUserDevices(user));
    }

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<ApiResponse<?>> deleteDevice(@PathVariable Long deviceId) {
        return ResponseEntity.ok(deviceService.deleteDevice(deviceId));
    }
}
