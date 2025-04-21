package com.example.pdponline.repository;

import com.example.pdponline.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module,Long> {
    boolean existsByName(String name);

    List<Module> findByCourseIdAndActive(Long courseId,boolean active);
    Module getByCourse_Id(Long courseId);
}
