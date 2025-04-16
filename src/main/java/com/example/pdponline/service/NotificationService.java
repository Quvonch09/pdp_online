package com.example.pdponline.service;

import com.example.pdponline.entity.Notification;
import com.example.pdponline.entity.User;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.NotificationDTO;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.repository.NotificationRepository;
import com.example.pdponline.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;


    public Notification save(NotificationDTO notificationDTO) {
        User user = userRepository.findById(notificationDTO.getUserId()).orElseThrow(() ->
                RestException.restThrow(ResponseError.NOTFOUND("User")));
        Notification notification = Notification.builder()
                .title(notificationDTO.getTitle())
                .content(notificationDTO.getContent())
                .student(user)
                .build();
        return notificationRepository.save(notification);
    }
}
