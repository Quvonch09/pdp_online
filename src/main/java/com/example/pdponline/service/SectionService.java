package com.example.pdponline.service;

import com.example.pdponline.entity.Module;
import com.example.pdponline.entity.Section;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.mapper.SectionMapper;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.payload.req.SectionReq;
import com.example.pdponline.repository.ModuleRepository;
import com.example.pdponline.repository.SectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SectionService {

    private final SectionRepository sectionRepository;
    private final ModuleRepository moduleRepository;

    @Transactional
    public ApiResponse<?> createSection(SectionReq req) {
        log.info("Creating new section with title: {}", req.title());

        if (sectionRepository.existsByTitle(req.title())) {
            log.warn("Section with title '{}' already exists", req.title());
            throw RestException.restThrow(ResponseError.ALREADY_EXIST("Section"));
        }

        Module module = findModuleByIdOrThrow(req.moduleId());

        Section section = buildSection(req, module);
        sectionRepository.save(section);

        log.info("Section created successfully with title: {}", req.title());
        return ApiResponse.successResponse("Section yaratildi");
    }

    @Transactional
    public ApiResponse<?> update(Long id, String title, boolean alreadyOpen) {
        log.info("Updating section with id: {} and new title: {}", id, title);

        Section section = findSectionByIdOrThrow(id);
        section.setTitle(title);

        sectionRepository.save(section);
        log.info("Section with id: {} successfully updated", id);

        return ApiResponse.successResponse("Section yangilandi");
    }

    @Transactional
    public ApiResponse<?> changeActive(Long id, boolean active) {
        log.info("Changing active status for section with id: {} to {}", id, active);

        Section section = findSectionByIdOrThrow(id);
        section.setActive(active);

        sectionRepository.save(section);
        log.info("Section with id: {} active status changed to {}", id, active);

        return ApiResponse.successResponse("Active o'zgartirildi");
    }

//    @Cacheable(value = "sections", key = "'module_' + #moduleId + '_active_' + #active")
    public ApiResponse<?> getByModule(Long moduleId, boolean active) {
        log.info("Fetching sections for moduleId: {} with active status: {}", moduleId, active);

        Module module = findModuleByIdOrThrow(moduleId);
        List<Section> sections = sectionRepository.findAllByModuleIdAndActive(moduleId, active);

        if (sections.isEmpty()) {
            log.warn("No sections found for moduleId: {}", moduleId);
            throw RestException.restThrow(ResponseError.NOTFOUND("Sectionlar"));
        }

        log.info("Successfully fetched {} sections for moduleId: {}", sections.size(), moduleId);
        return ApiResponse.successResponse(SectionMapper.toDtoList(sections));
    }

//    @Cacheable(value = "section", key = "'section_' + #id")
    public ApiResponse<?> getById(Long id) {
        log.info("Fetching section by id: {}", id);

        Section section = findSectionByIdOrThrow(id);
        log.info("Successfully fetched section with id: {}", id);

        return ApiResponse.successResponse(SectionMapper.toDto(section));
    }

    private Section buildSection(SectionReq req, Module module) {
        return Section.builder()
                .title(req.title())
                .module(module)
                .alreadyOpen(req.alreadyOpen())
                .active(true)
                .build();
    }

    private Module findModuleByIdOrThrow(Long moduleId) {
        return moduleRepository.findById(moduleId)
                .orElseThrow(() -> RestException.restThrow(ResponseError.NOTFOUND("Module")));
    }

    private Section findSectionByIdOrThrow(Long sectionId) {
        return sectionRepository.findById(sectionId)
                .orElseThrow(() -> RestException.restThrow(ResponseError.NOTFOUND("Section")));
    }
}
