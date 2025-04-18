package com.example.pdponline.service;

import com.example.pdponline.entity.Category;
import com.example.pdponline.entity.Course;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.mapper.CourseMapper;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.payload.req.CourseReq;
import com.example.pdponline.repository.CategoryRepository;
import com.example.pdponline.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;

//    @CacheEvict(value = {"courseById", "coursesByCategory"}, allEntries = true)
    public ApiResponse<?> createCourse(CourseReq courseReq) {
        log.info("Creating course with name: {}", courseReq.name());

        Category category = categoryRepository.findById(courseReq.categoryId())
                .orElseThrow(() -> RestException.restThrow(ResponseError.NOTFOUND("Category")));

        if (!category.isActive())
            throw RestException.restThrow(ResponseError.NOTFOUND("Category"));

        if (courseRepository.existsByName(courseReq.name()))
            throw RestException.restThrow(ResponseError.ALREADY_EXIST("Course"));

        Course course = Course.builder()
                .name(courseReq.name())
                .category(category)
                .active(true)
                .build();

        courseRepository.save(course);
        log.info("Course saved: {}", course.getName());

        return ApiResponse.successResponse("Course added");
    }

//    @CacheEvict(value = {"courseById", "coursesByCategory"}, allEntries = true)
    public ApiResponse<?> updateCourse(Long id, String name) {
        Course course = getCourseOrThrow(id);

        if (courseRepository.existsByName(name))
            throw RestException.restThrow(ResponseError.ALREADY_EXIST("Course"));

        course.setName(name);
        courseRepository.save(course);
        log.info("Course updated: id={}, newName={}", id, name);

        return ApiResponse.successResponse("Course nomi yangilandi");
    }

//    @CacheEvict(value = {"courseById", "coursesByCategory"}, allEntries = true)
    public ApiResponse<?> changeActive(Long id, boolean active) {
        Course course = getCourseOrThrow(id);

        course.setActive(active);
        courseRepository.save(course);
        log.info("Course active status changed: id={}, active={}", id, active);

        return ApiResponse.successResponse("Active o'zgartirildi");
    }

//    @Cacheable(value = "coursesByCategory", key = "#id + '_' + #active")
    public ApiResponse<?> getByCategory(Long id, boolean active) {
        log.info("Fetching courses by categoryId={} and active={}", id, active);

        categoryRepository.findById(id)
                .orElseThrow(() -> RestException.restThrow(ResponseError.NOTFOUND("Category")));

        List<Course> courses = courseRepository.findByCategoryIdAndActive(id, active);
        if (courses.isEmpty())
            throw RestException.restThrow(ResponseError.NOTFOUND("Kurslar"));
        return ApiResponse.successResponse(CourseMapper.toDtoList(courses));
    }

//    @Cacheable(value = "courseById", key = "#id")
    public ApiResponse<?> getCourse(Long id) {
        log.info("Fetching course by id: {}", id);

        Course course = getCourseOrThrow(id);
        return ApiResponse.successResponse(CourseMapper.toDto(course));
    }

    public ApiResponse<?> getByActive(boolean active) {
        List<Course> courses = courseRepository.findByActive(active);

        if (courses.isEmpty())
            throw RestException.restThrow(ResponseError.NOTFOUND("Kurslar"));

        return ApiResponse.successResponse(CourseMapper.toDtoList(courses));
    }

//    @Async
//    public CompletableFuture<Course> getCourseAsync(Long id) {
//        return CompletableFuture.completedFuture(getCourseOrThrow(id));
//    }

    private Course getCourseOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> RestException.restThrow(ResponseError.NOTFOUND("Course")));
    }
}
