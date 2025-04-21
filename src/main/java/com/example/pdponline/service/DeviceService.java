package com.example.pdponline.service;

import com.example.pdponline.entity.DeviceInfo;
import com.example.pdponline.entity.User;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.DeviceInfoDTO;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.payload.res.ResNotification;
import com.example.pdponline.repository.DeviceInfoRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ua_parser.Client;
import ua_parser.Parser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceInfoRepository deviceInfoRepository;
    private final RedisTokenService redisTokenService;
    private final NotificationService notificationService;

    @SneakyThrows
    @CacheEvict(value = "devices", allEntries = true)
    public Long handleLogin(HttpServletRequest request, User user) {
        String userAgent = request.getHeader("User-Agent");

        Parser parser = new Parser();
        Client client = parser.parse(userAgent);

        List<DeviceInfo> existingDevices = deviceInfoRepository.findAllByUserOrderByLoginTimeAsc(user);

        if (deviceInfoRepository.existsByUserAndDeviceAgent(user, userAgent)) {
            return deviceInfoRepository.findByUserAndDeviceAgent(user, userAgent).getId();
        }

        if (existingDevices.size() >= 2) {
            return 0L;
        }

            DeviceInfo newDevice = new DeviceInfo(
                    userAgent,
                    client.userAgent.family + " " + client.userAgent.major,
                    client.os.family,
                    client.device.family,
                    LocalDateTime.now(),
                    user
            );

            newDevice.setMain(existingDevices.isEmpty());


            DeviceInfo savedDevice = deviceInfoRepository.save(newDevice);

            if (!newDevice.isMain()) {
                Optional<DeviceInfo> mainDeviceOpt = existingDevices.stream()
                        .filter(DeviceInfo::isMain)
                        .findFirst();

                mainDeviceOpt.ifPresent(mainDevice -> {
                    notificationService.sendNotification(
                            user.getId(),
                            mainDevice.getId(),
                            ResNotification.builder()
                                    .title("Bildirishnoma!")
                                    .content("Siz " + newDevice.getDeviceAgent() + " qurilmasidan saytga kirdingiz")
                                    .build()
                    );
                });
            }
            return savedDevice.getId();
    }



    @Cacheable(value = "devices", key = "'user_' + #user")
    public ApiResponse<List<DeviceInfoDTO>> getUserDevices(User user) {
        List<DeviceInfoDTO> list = deviceInfoRepository.findAllByUserOrderByLoginTimeAsc(user).stream()
                .map(this::convertDto).toList();

        return ApiResponse.successResponse(list);
    }

    public ApiResponse<?> deleteDevice(Long deviceId){
        DeviceInfo deviceInfo = deviceInfoRepository.findById(deviceId).orElseThrow(
                () -> RestException.restThrow(ResponseError.NOTFOUND("Device"))
        );

        if (!deviceInfo.isMain()) {
            deviceInfoRepository.delete(deviceInfo);

            redisTokenService.removeToken(deviceId);

            return ApiResponse.successResponse("Device removed and logged out");
        }

        return ApiResponse.successResponse("Main device don't delete");
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
                .mainDevice(deviceInfo.isMain())
                .build();
    }
}
