package com.example.pdponline.repository;

import com.example.pdponline.entity.TaskResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskResultRepository extends JpaRepository<TaskResult,Long> {

    List<TaskResult> findByTask_Id(Long taskId);
    List<TaskResult> findByStudent_Id(Long studentId);
    @Query(value = """
            SELECT
                tr.*
            FROM
                task_result tr
                    JOIN
                task t ON tr.task_id = t.id
                    JOIN
                lesson l ON t.lesson_id = l.id
            WHERE
                tr.student_id = :studentId and l.id = :lessonId
            """,nativeQuery = true)
    List<TaskResult> findByStudentIdAndLesson_Id(Long studentId, Long lessonId);
}
