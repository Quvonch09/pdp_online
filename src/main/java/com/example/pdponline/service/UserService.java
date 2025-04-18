package com.example.pdponline.service;

import com.example.pdponline.entity.File;
import com.example.pdponline.entity.User;
import com.example.pdponline.entity.enums.Role;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.payload.UserDTO;
import com.example.pdponline.payload.auth.ResponseLogin;
import com.example.pdponline.payload.res.ResPageable;
import com.example.pdponline.payload.res.ResUser;
import com.example.pdponline.repository.FileRepository;
import com.example.pdponline.repository.UserRepository;
import com.example.pdponline.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;


    public ApiResponse<?> getMe(User user){
        return ApiResponse.successResponse(covertDTO(user));
    }


    public ApiResponse<?> updateUser(Long userId, ResUser resUser){

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
            String token = jwtProvider.generateToken(save.getPhoneNumber());
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
            userList.add(covertDTO(user));
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



    public UserDTO covertDTO(User user){
        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().name())
                .imgId(user.getImgUrl() != null ? user.getImgUrl() : null)
                .build();
    }
}
