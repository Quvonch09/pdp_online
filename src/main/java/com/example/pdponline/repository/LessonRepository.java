package com.example.pdponline.repository;

import com.example.pdponline.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findAllBySectionIdAndActiveIsTrue(Long sectionId);

    @Query(value = "select img_urls from lesson_img_urls img where lesson_id = ?1", nativeQuery = true)
    List<String> findAllImgUrlByLessonId(Long lessonId);
}
