package com.example.pdponline.repository;

import com.example.pdponline.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByStudentId(Long studentId);

    Integer countAllByStudentIdAndReadIsFalse(Long userId);

}
