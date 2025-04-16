package com.example.pdponline.repository;

import com.example.pdponline.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course,Long> {
    boolean existsByName(String name);

    List<Course> findByCategoryIdAndActive(Long id,boolean active);

    List<Course> findByActive(boolean active);
}
