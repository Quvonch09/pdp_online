package com.example.pdponline.repository;

import com.example.pdponline.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findAllBySectionIdAndActiveIsTrue(Long sectionId);
}
