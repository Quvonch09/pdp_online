package com.example.pdponline.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record StatisticDto (
        Long countStudents,
        Long countTeachers,
        Long countAssistantTeachers,
        Long countAllUsers,
        Double sumSuccessPayments,
        Double sumFailedPayments,
        Double sumReturnedPayments,
        Long countCategory,
        Long countCourses,
        Long countUnCheckedTasks
){
}
