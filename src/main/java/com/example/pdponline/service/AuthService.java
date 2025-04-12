package com.example.pdponline.service;

import com.example.pdponline.entity.enums.Role;
import com.example.pdponline.payload.auth.AuthRegister;
import com.example.pdponline.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.pdponline.entity.User;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.payload.auth.AuthLogin;
import com.example.pdponline.payload.auth.ResponseLogin;
import com.example.pdponline.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;


    public ApiResponse login(AuthLogin authLogin)
    {
        User user = userRepository.findByPhoneNumber(authLogin.getPhoneNumber());
        if (user == null) {
            return new ApiResponse(ResponseError.NOTFOUND("User"));
        }

        if (!user.isEnabled()){
            return new ApiResponse(ResponseError.ACCESS_DENIED());
        }

        if (passwordEncoder.matches(authLogin.getPassword(), user.getPassword())) {
            String token = jwtProvider.generateToken(authLogin.getPhoneNumber());
            ResponseLogin responseLogin = new ResponseLogin(token, user.getRole().name(), user.getId());
            return new ApiResponse(responseLogin);
        }

        return new ApiResponse(ResponseError.PASSWORD_DID_NOT_MATCH());
    }

    public ApiResponse register(AuthRegister auth)
    {

        User byPhoneNumber = userRepository.findByPhoneNumberAndEnabledTrue(auth.getPhoneNumber());
        if (byPhoneNumber != null) {
            return new ApiResponse(ResponseError.ALREADY_EXIST("Phone number"));
        }

        saveUser(auth, Role.ROLE_STUDENT);
        return new ApiResponse("Success");
    }



    public ApiResponse forgotPassword(AuthLogin authLogin){
        User byPhoneNumber = userRepository.findByPhoneNumberAndEnabledTrue(authLogin.getPhoneNumber());
        if (byPhoneNumber == null) {
            return new ApiResponse(ResponseError.NOTFOUND("USER"));
        }

        byPhoneNumber.setPassword(passwordEncoder.encode(authLogin.getPassword()));
        userRepository.save(byPhoneNumber);
        return new ApiResponse("Success");
    }



    private void saveUser(AuthRegister auth, Role role)
    {
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
