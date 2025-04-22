package com.example.pdponline.repository;

import com.example.pdponline.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByLessonId(long id);

    @Query(value = "select attachments from task_attachments where task_id = ?1", nativeQuery = true)
    List<String> findAllByTaskUrls(Long taskId);
}
