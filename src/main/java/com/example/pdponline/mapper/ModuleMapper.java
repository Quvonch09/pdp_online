package com.example.pdponline.mapper;

import com.example.pdponline.entity.Module;
import com.example.pdponline.payload.ModuleDto;
import java.util.List;

public class ModuleMapper {

    public static ModuleDto toDto(Module module){
        return ModuleDto.builder()
                .id(module.getId())
                .name(module.getName())
                .courseId(module.getCourse().getId())
                .price(module.getPrice())
                .active(module.isActive())
                .build();
    }

    public static List<ModuleDto> toDtoList(List<Module> modules){
        return modules.stream()
                .map(ModuleMapper::toDto)
                .toList();
    }
}
