package com.example.pdponline.repository;

import com.example.pdponline.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section,Long> {
    boolean existsByTitle(String title);

    List<Section> findAllByModuleIdAndActive(Long moduleId,boolean active);
}
