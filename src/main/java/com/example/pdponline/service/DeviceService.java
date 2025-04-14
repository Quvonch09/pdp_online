package com.example.pdponline.service;

import com.example.pdponline.entity.DeviceInfo;
import com.example.pdponline.entity.User;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.DeviceInfoDTO;
import com.example.pdponline.repository.DeviceInfoRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua_parser.Client;
import ua_parser.Parser;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceInfoRepository deviceInfoRepository;

    public void handleLogin(HttpServletRequest request, User user) throws Exception {
        String userAgent = request.getHeader("User-Agent");
        Parser parser = new Parser();
        Client client = parser.parse(userAgent);

        if (!deviceInfoRepository.existsByUserAndDeviceAgent(user, userAgent)) {
            List<DeviceInfo> devices = deviceInfoRepository.findAllByUserOrderByLoginTimeAsc(user);

            if (devices.size() >= 2) {
                DeviceInfo oldest = devices.get(0);
                deviceInfoRepository.delete(oldest);
            }

            DeviceInfo newDevice = new DeviceInfo(
                    userAgent,
                    client.userAgent.family + " " + client.userAgent.major,
                    client.os.family,
                    client.device.family,
                    LocalDateTime.now(),
                    user
            );

            deviceInfoRepository.save(newDevice);
        }
    }

    public ApiResponse<List<DeviceInfoDTO>> getUserDevices(User user) {
        List<DeviceInfoDTO> list = deviceInfoRepository.findAllByUserOrderByLoginTimeAsc(user).stream()
                .map(this::convertDto).toList();

        return ApiResponse.successResponse(list);
    }


    public DeviceInfoDTO convertDto(DeviceInfo deviceInfo) {
        return DeviceInfoDTO.builder()
                .id(deviceInfo.getId())
                .loginTime(deviceInfo.getLoginTime())
                .deviceAgent(deviceInfo.getDeviceAgent())
                .userId(deviceInfo.getUser().getId())
                .deviceType(deviceInfo.getDeviceType())
                .os(deviceInfo.getOs())
                .browser(deviceInfo.getBrowser())
                .build();
    }
}
