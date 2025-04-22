package com.example.pdponline.repository;

import com.example.pdponline.entity.LessonAttachments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonAttachmentRepository extends JpaRepository<LessonAttachments, Long> {
    LessonAttachments findByLessonId(Long lessonId);
}
