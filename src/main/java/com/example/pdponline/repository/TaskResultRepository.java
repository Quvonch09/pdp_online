package com.example.pdponline.repository;

import com.example.pdponline.entity.TaskResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskResultRepository extends JpaRepository<TaskResult,Long> {

    List<TaskResult> findByTask_Id(Long taskId);
    List<TaskResult> findByStudent_Id(Long studentId);
}
