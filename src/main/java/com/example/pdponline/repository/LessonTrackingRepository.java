package com.example.pdponline.repository;

import com.example.pdponline.entity.LessonTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonTrackingRepository extends JpaRepository<LessonTracking,Long> {
    List<LessonTracking> findAllByStudentId(Long studentId);
}
