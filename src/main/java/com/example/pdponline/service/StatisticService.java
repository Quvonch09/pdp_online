package com.example.pdponline.service;

import com.example.pdponline.entity.enums.PaymentStatus;
import com.example.pdponline.entity.enums.Role;
import com.example.pdponline.mapper.StatisticMapper;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.StatisticDto;
import com.example.pdponline.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticService {

    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final CategoryRepository categoryRepository;
    private final CourseRepository courseRepository;
    private final TaskRepository taskRepository;

    @Cacheable(value = "statistic")
    public ApiResponse<?> ceoDashboard(){
        long countTeachers = userRepository.countAllByRoleAndEnabledTrue(Role.ROLE_TEACHER);
        long countStudent = userRepository.countAllByRoleAndEnabledTrue(Role.ROLE_STUDENT);
        long countAssistantTeachers = userRepository.countAllByRoleAndEnabledTrue(Role.ROLE_TEACHER_ASSISTANT);
        long countAllUsers = userRepository.count();
        double sumSuccessPayments = paymentRepository.getPaymentSum(PaymentStatus.SUCCESS);
        double sumFailedPayments = paymentRepository.getPaymentSum(PaymentStatus.FAILED);
        double sumReturnedPayments = paymentRepository.getPaymentSum(PaymentStatus.RETURNED);
        long countCategory = categoryRepository.count();
        long countCourses = courseRepository.count();
//        long countUncheckedTasks =

        return ApiResponse.successResponse(StatisticMapper.toDto(
                countAllUsers,
                countStudent,
                countTeachers,
                countAssistantTeachers,
                countCategory,
                countCourses,
                sumSuccessPayments,
                sumFailedPayments,
                sumReturnedPayments,
                null
        ));
    }
}
