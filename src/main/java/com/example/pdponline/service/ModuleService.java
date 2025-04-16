package com.example.pdponline.service;

import com.example.pdponline.entity.Course;
import com.example.pdponline.entity.Module;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.mapper.ModuleMapper;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.payload.req.ModuleReq;
import com.example.pdponline.repository.CourseRepository;
import com.example.pdponline.repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;

    @CacheEvict(value = "modules", allEntries = true)
    public ApiResponse<String> createModule(ModuleReq req) {
        if (moduleRepository.existsByName(req.name())) {
            log.warn("Module already exists: {}", req.name());
            throw RestException.restThrow(ResponseError.ALREADY_EXIST("Module"));
        }

        Course course = courseRepository.findById(req.courseId())
                .orElseThrow(() -> {
                    log.error("Course not found with id: {}", req.courseId());
                    return RestException.restThrow(ResponseError.NOTFOUND("Course"));
                });

        if (!course.isActive()) {
            log.warn("Course is inactive: {}", req.courseId());
            throw RestException.restThrow(ResponseError.NOTFOUND("Course"));
        }

        Module module = Module.builder()
                .name(req.name())
                .course(course)
                .price(req.price())
                .active(true)
                .build();

        moduleRepository.save(module);
        log.info("Module created: {}", module.getName());
        return ApiResponse.successResponseForMsg("Module added");
    }

    @CacheEvict(value = "modules", allEntries = true)
    public ApiResponse<String> updateModule(Long id, String name) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Module not found with id: {}", id);
                    return RestException.restThrow(ResponseError.NOTFOUND("Module"));
                });

        if (moduleRepository.existsByName(name)) {
            log.warn("Module already exists with name: {}", name);
            throw RestException.restThrow(ResponseError.ALREADY_EXIST("Module"));
        }

        module.setName(name);
        moduleRepository.save(module);
        log.info("Module updated: {}", module.getId());
        return ApiResponse.successResponse("Module updated");
    }

    @CacheEvict(value = "modules", allEntries = true)
    public ApiResponse<String> changeActive(Long id, boolean active) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Module not found with id: {}", id);
                    return RestException.restThrow(ResponseError.NOTFOUND("Module"));
                });

        module.setActive(active);
        moduleRepository.save(module);
        log.info("Module status changed: {} -> {}", id, active);
        return ApiResponse.successResponse("Module status changed");
    }

    @Cacheable(value = "modules", key = "'course_' + #id + '_active_' + #active")
    public ApiResponse<?> getByCourse(Long id, boolean active) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Course not found with id: {}", id);
                    return RestException.restThrow(ResponseError.NOTFOUND("Course"));
                });

        List<Module> modules = moduleRepository.findByCourseIdAndActive(course.getId(), active);
        if (CollectionUtils.isEmpty(modules)) {
            log.warn("No modules found for course with id: {} and active status: {}", id, active);
            throw RestException.restThrow(ResponseError.NOTFOUND("Module"));
        }

        return ApiResponse.successResponse(ModuleMapper.toDtoList(modules));
    }

    @Cacheable(value = "modules", key = "#id")
    public ApiResponse<?> getById(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Module not found with id: {}", id);
                    return RestException.restThrow(ResponseError.NOTFOUND("Module"));
                });
        return ApiResponse.successResponse(ModuleMapper.toDto(module));
    }
}
