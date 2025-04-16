package com.example.pdponline.mapper;

import com.example.pdponline.entity.Course;
import com.example.pdponline.payload.CourseDto;
import java.util.List;
import java.util.stream.Collectors;

public class CourseMapper {

    public static CourseDto toDto(Course course) {
        return new CourseDto(
                course.getId(),
                course.getName(),
                course.getCategory().getId(),
                course.getCategory().getName(),
                course.isActive(),
                course.getCreatedAt()
        );
    }

    public static List<CourseDto> toDtoList(List<Course> courses) {
        return courses.stream()
                .map(CourseMapper::toDto)
                .collect(Collectors.toList());
    }
}
