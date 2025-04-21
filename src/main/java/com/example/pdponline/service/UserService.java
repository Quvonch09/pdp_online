package com.example.pdponline.service;

import com.example.pdponline.entity.DeviceInfo;
import com.example.pdponline.entity.File;
import com.example.pdponline.entity.User;
import com.example.pdponline.entity.enums.ChatStatus;
import com.example.pdponline.entity.enums.Role;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.payload.UserDTO;
import com.example.pdponline.payload.auth.ResponseLogin;
import com.example.pdponline.payload.res.ResPageable;
import com.example.pdponline.payload.res.ResUser;
import com.example.pdponline.repository.DeviceInfoRepository;
import com.example.pdponline.repository.FileRepository;
import com.example.pdponline.repository.UserRepository;
import com.example.pdponline.security.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final DeviceInfoRepository deviceInfoRepository;
    private final DeviceService deviceService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisTokenService redisTokenService;


    public ApiResponse<?> getMe(HttpServletRequest request, User user){
        String userAgent = request.getHeader("User-Agent");
        DeviceInfo deviceInfo = deviceInfoRepository
                .findByUserAndDeviceAgent(user, userAgent);


        return ApiResponse.successResponse(covertDTO(user, deviceInfo));
    }


    public ApiResponse<?> updateUser(HttpServletRequest request, Long userId, ResUser resUser){

        User user = userRepository.findById(userId).orElse(null);
        if(user == null){
            throw RestException.restThrow(ResponseError.NOTFOUND("User"));
        }

        boolean phoneChanged = !user.getPhoneNumber().equals(resUser.getPhoneNumber());


        user.setFirstName(resUser.getFirstName());
        user.setLastName(resUser.getLastName());
        user.setImgUrl(resUser.getImgUrl());
        user.setPhoneNumber(resUser.getPhoneNumber());

        if (resUser.getPassword() != null && !resUser.getPassword().isEmpty()) {
            if (!passwordEncoder.matches(resUser.getPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(resUser.getPassword()));
            }
        }

        User save = userRepository.save(user);


        if (phoneChanged) {
            Long deviceId = deviceService.handleLogin(request, user);// <-- BU YERDA

            Map<String, Object> claims = new HashMap<>();
            claims.put("deviceId", deviceId);

            String token = jwtProvider.generateToken(claims, save.getPhoneNumber());

            redisTokenService.removeToken(deviceId);
            redisTokenService.saveToken(deviceId, token, Duration.ofHours(1));

            ResponseLogin responseLogin = new ResponseLogin(token, save.getRole().name(), save.getId());
            return ApiResponse.successResponse(responseLogin);
        }

        return ApiResponse.successResponse("User successfully updated");

    }


    public ApiResponse<?> searchUsers(String keyword,
                                      String phoneNumber,
                                      Role role,
                                      int page, int size){
        Page<User> users = userRepository.searchUsers(keyword, phoneNumber, role.name(), PageRequest.of(page, size));

        if (users.getTotalElements() == 0) {
            throw RestException.restThrow(ResponseError.NOTFOUND("Users"));
        }

        List<UserDTO> userList = new ArrayList<>();
        for (User user : users) {
            userList.add(covertDTO(user, null));
        }

        ResPageable resPageable = ResPageable.builder()
                .page(page)
                .size(size)
                .totalElements(users.getTotalElements())
                .totalPage(users.getTotalPages())
                .body(userList)
                .build();

        return ApiResponse.successResponse(resPageable);
    }


    public void onlineOffline(User user, boolean isActive) {
        ChatStatus newStatus = isActive ? ChatStatus.ONLINE : ChatStatus.OFFLINE;
        if (user.getChatStatus() != newStatus) {
            user.setChatStatus(newStatus);
            userRepository.save(user);
        }
    }


    public List<User> searchForChat(String fullName, String phone, String roleName) {
        List<User> users = userRepository.searchForChat(fullName, phone, roleName);
        return users != null ? users : new ArrayList<>();  // Agar null bo‘lsa, bo‘sh list qaytarish
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);

    }



    public UserDTO covertDTO(User user, DeviceInfo deviceInfo){
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().name())
                .imgUrl(user.getImgUrl() != null ? user.getImgUrl() : null)
                .deviceId(deviceInfo !=null ? deviceInfo.getId() : null)
                .build();
    }
}
