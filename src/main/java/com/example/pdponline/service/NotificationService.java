package com.example.pdponline.service;

import com.example.pdponline.entity.Notification;
import com.example.pdponline.entity.User;
import com.example.pdponline.entity.enums.Role;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.IdList;
import com.example.pdponline.payload.NotificationDTO;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.payload.res.ResNotification;
import com.example.pdponline.repository.NotificationRepository;
import com.example.pdponline.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;


    public ApiResponse<String> adminSendNotificationAllStudents(ResNotification notificationDTO) {

        List<User> students = userRepository.findAllByRoleAndEnabledTrue(Role.ROLE_STUDENT);
        if (students.isEmpty()){
            throw RestException.restThrow(ResponseError.NOTFOUND("Studentlar"));
        }

        for (User student : students) {
            Notification notification = Notification.builder()
                    .title(notificationDTO.getTitle())
                    .content(notificationDTO.getContent())
                    .student(student)
                    .read(false)
                    .build();
            notificationRepository.save(notification);
        }
        return ApiResponse.successResponse("Notification successfully sending");
    }



    public ApiResponse<?> getMyNotifications(User student) {
        List<NotificationDTO> notificationDTOS = notificationRepository.findAllByStudentId(student.getId()).stream()
                .map(this::convertDtoToNotification).toList();

        return ApiResponse.successResponse(notificationDTOS);
    }


    public ApiResponse<?> getCountUnreadNotifications(User user) {

        Integer counted = notificationRepository.countAllByStudentIdAndReadIsFalse(user.getId());
        return ApiResponse.successResponse(counted);

    }



    public ApiResponse<?> isReadNotification(IdList idList) {
        if (idList.getIds().isEmpty()){
            return ApiResponse.errorResponse("List bush bulmasin",400);
        }

        List<Notification> allById = notificationRepository.findAllById(idList.getIds());
        allById.forEach(notification -> {notification.setRead(true);
            notificationRepository.save(notification);});

        return ApiResponse.successResponse("Successfully read notifications");
    }


    public ApiResponse<?> deleteNotification(Long id) {

        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification == null) {
           throw RestException.restThrow(ResponseError.NOTFOUND("Notification"));
        }

        notificationRepository.delete(notification);
        return ApiResponse.successResponse("Success");
    }


    public NotificationDTO convertDtoToNotification(Notification notification)
    {
        return new NotificationDTO(
                notification.getId(),
                notification.getTitle(),
                notification.getContent(),
                notification.getStudent().getId(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
