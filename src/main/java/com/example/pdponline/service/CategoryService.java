package com.example.pdponline.service;

import com.example.pdponline.entity.Category;
import com.example.pdponline.entity.User;
import com.example.pdponline.entity.enums.CategoryType;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.mapper.CategoryMapper;
import com.example.pdponline.payload.*;
import com.example.pdponline.payload.req.CategoryReq;
import com.example.pdponline.repository.CategoryRepository;
import com.example.pdponline.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @CacheEvict(value = "categories", allEntries = true)
    public ApiResponse<String> addCategory(CategoryType type, CategoryReq req) {
        if (categoryRepository.existsByName(req.name()))
            throw RestException.restThrow(ResponseError.ALREADY_EXIST("Category"));

        List<User> mentors = getMentorsByIds(req.mentorIds());

        Category category = Category.builder()
                .name(req.name())
                .description(req.description())
                .duration(req.duration())
                .active(true)
                .categoryType(type)
                .mentors(mentors)
                .build();

        categoryRepository.save(category);
        log.info("Category created: {}", category.getName());
        return ApiResponse.successResponseForMsg("Category added");
    }

    @CacheEvict(value = "categories", allEntries = true)
    public ApiResponse<String> updateCategory(Long id, CategoryReq req) {
        Category category = getCategoryOrThrow(id);

        List<User> mentors = CollectionUtils.isEmpty(req.mentorIds())
                ? category.getMentors()
                : getMentorsByIds(req.mentorIds());

        category.setName(req.name());
        category.setDescription(req.description());
        category.setDuration(req.duration());
        category.setMentors(mentors);

        categoryRepository.save(category);
        log.info("Category updated: {}", category.getId());
        return ApiResponse.successResponse("Category updated");
    }

    @CacheEvict(value = "categories", allEntries = true)
    public ApiResponse<String> changeActive(boolean active, Long id) {
        Category category = getCategoryOrThrow(id);
        category.setActive(active);
        categoryRepository.save(category);
        log.info("Category status changed: {} -> {}", category.getId(), active);
        return ApiResponse.successResponse("Category status changed");
    }

    @Cacheable(value = "categories", key = "#id")
    public ApiResponse<?> getCategory(Long id) {
        Category category = getCategoryOrThrow(id);
        List<MentorDto> mentorDtos = buildMentorDtos(category.getMentors());
        return ApiResponse.successResponse(CategoryMapper.toDto(mentorDtos, category));
    }

    @Cacheable(value = "categories", key = "'all'")
    public ApiResponse<?> getAllCategory() {
        List<Category> categories = getNonEmptyCategoryList(categoryRepository.findAll());
        return ApiResponse.successResponse(toDtoListWithMentors(categories));
    }

    @Cacheable(value = "categories", key = "'active_' + #active")
    public ApiResponse<?> getByActive(boolean active) {
        List<Category> categories = getNonEmptyCategoryList(categoryRepository.findByActive(active));
        return ApiResponse.successResponse(toDtoListWithMentors(categories));
    }

    @Cacheable(value = "categories", key = "'type_' + #type")
    public ApiResponse<?> getByType(CategoryType type) {
        List<Category> categories = getNonEmptyCategoryList(categoryRepository.findByCategoryType(type));
        return ApiResponse.successResponse(toDtoListWithMentors(categories));
    }

//    @Async
//    public CompletableFuture<List<MentorDto>> getMentorDtosAsync(List<User> mentors) {
//        return CompletableFuture.supplyAsync(() -> buildMentorDtos(mentors));
//    }

    protected List<MentorDto> buildMentorDtos(List<User> mentors) {
        return mentors.stream().map(mentor ->
                MentorDto.builder()
                        .id(mentor.getId())
                        .firstname(mentor.getFirstName())
                        .lastname(mentor.getLastName())
                        .imgId(mentor.getImgUrl() != null ? mentor.getImgUrl() : null)
                        .role(mentor.getRole())
                        .build()
        ).collect(Collectors.toList());
    }

    private List<User> getMentorsByIds(List<Long> ids) {
        List<User> mentors = userRepository.findAllById(ids);
        if (mentors.isEmpty())
            throw RestException.restThrow(ResponseError.NOTFOUND("Mentorlar"));
        return mentors;
    }

    private Category getCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> RestException.restThrow(ResponseError.NOTFOUND("Category")));
    }

    private List<Category> getNonEmptyCategoryList(List<Category> list) {
        if (list.isEmpty())
            throw RestException.restThrow(ResponseError.NOTFOUND("Category"));
        return list;
    }

    protected List<CategoryDto> toDtoListWithMentors(List<Category> categories) {
        return categories.stream()
                .map(category -> CategoryMapper.toDto(buildMentorDtos(category.getMentors()), category))
                .collect(Collectors.toList());
    }
}
