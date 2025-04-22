package com.example.pdponline.repository;

import com.example.pdponline.entity.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedBackRepository extends JpaRepository<Feedback,Long> {

    Optional<Feedback> findByStudent_IdAndLesson_Id(Long studentId, Long lessonId);

    Page<Feedback> getAllByLesson_Id(Long lessonId, Pageable pageable);

}
