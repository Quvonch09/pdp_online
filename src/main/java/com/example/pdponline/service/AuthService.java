package com.example.pdponline.service;

import com.example.pdponline.entity.User;
import com.example.pdponline.entity.enums.Role;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.payload.auth.AuthLogin;
import com.example.pdponline.payload.auth.AuthRegister;
import com.example.pdponline.payload.auth.ResponseLogin;
import com.example.pdponline.repository.UserRepository;
import com.example.pdponline.security.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final DeviceService deviceService;

    @SneakyThrows
    public ApiResponse<ResponseLogin> login(AuthLogin authLogin, HttpServletRequest request) {
        User user = userRepository.findByPhoneNumber(authLogin.getPhoneNumber()).orElseThrow(
                () -> RestException.restThrow(ResponseError.NOTFOUND("User"))
        );

        if (!user.isEnabled()) {
            throw RestException.restThrow(ResponseError.ACCESS_DENIED());
        }

        if (passwordEncoder.matches(authLogin.getPassword(), user.getPassword())) {
            String token = jwtProvider.generateToken(authLogin.getPhoneNumber());

            deviceService.handleLogin(request, user); // <-- BU YERDA

            ResponseLogin responseLogin = new ResponseLogin(token, user.getRole().name(), user.getId());
            return ApiResponse.successResponse(responseLogin);
        }

        throw RestException.restThrow(ResponseError.PASSWORD_DID_NOT_MATCH());
    }

    public ApiResponse<String> register(AuthRegister auth) {

        Optional<User> optionalUser = userRepository.findByPhoneNumber(auth.getPhoneNumber());
        if (optionalUser.isPresent()) {
            throw RestException.restThrow(ResponseError.ALREADY_EXIST("Phone Number"));
        }
        saveUser(auth, Role.ROLE_STUDENT);
        return ApiResponse.successResponse("Success");
    }

    public ApiResponse<String> forgotPassword(AuthLogin authLogin) {
        User byPhoneNumber = userRepository.findByPhoneNumber(authLogin.getPhoneNumber())
                .orElseThrow(() -> RestException.restThrow(ResponseError.NOTFOUND("USER")));

        byPhoneNumber.setPassword(passwordEncoder.encode(authLogin.getPassword()));
        userRepository.save(byPhoneNumber);
        return ApiResponse.successResponse("Success");
    }

    private void saveUser(AuthRegister auth, Role role) {
        User user = User.builder()
                .firstName(auth.getFirstName())
                .lastName(auth.getLastName())
                .phoneNumber(auth.getPhoneNumber())
                .password(passwordEncoder.encode(auth.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);
    }

}
