package com.example.pdponline.mapper;

import com.example.pdponline.payload.StatisticDto;

public class StatisticMapper {

    public static StatisticDto toDto(
            Long countAllUsers,
            Long countStudents,
            Long countTeachers,
            Long countAssistantTeachers,
            Long countCategory,
            Long countCourses,
            Double sumSuccessPayments,
            Double sumFailedPayments,
            Double sumReturnedPayments,
            Long countUncheckedTasks
    ){
        return StatisticDto.builder()
                .countAllUsers(countAllUsers)
                .countStudents(countStudents)
                .countTeachers(countTeachers)
                .countAssistantTeachers(countAssistantTeachers)
                .countUnCheckedTasks(countUncheckedTasks)
                .countCategory(countCategory)
                .countCourses(countCourses)
                .sumSuccessPayments(sumSuccessPayments)
                .sumFailedPayments(sumFailedPayments)
                .sumReturnedPayments(sumReturnedPayments)
                .build();
    }
}
