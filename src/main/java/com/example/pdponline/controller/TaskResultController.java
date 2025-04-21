package com.example.pdponline.controller;

import com.example.pdponline.payload.TaskResultDTO;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.service.TaskResultService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task-result")
public class TaskResultController {
    private final TaskResultService taskResultService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_TEACHER','ROLE_TEACHER_ASSISTANT')")
    public ResponseEntity<ApiResponse<String>> createTaskResult(@Valid @RequestBody TaskResultDTO taskResultDTO) {
        return ResponseEntity.ok(taskResultService.createTaskResult(taskResultDTO));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_TEACHER','ROLE_TEACHER_ASSISTANT')")
    public ResponseEntity<ApiResponse<String>> updateTaskResult(@PathVariable Long id, @Valid @RequestBody TaskResultDTO taskResultDTO) {
        return ResponseEntity.ok(taskResultService.updateTaskResult(id, taskResultDTO));
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "id orqali task resultni olish")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_TEACHER','ROLE_TEACHER_ASSISTANT')")
    public ResponseEntity<ApiResponse<TaskResultDTO>> getTaskResult(@PathVariable Long id) {
        return ResponseEntity.ok(taskResultService.getTaskResult(id));
    }

    @GetMapping("/get-by-task/{taskId}")
    @Operation(summary = "Task orqali task resultlarni olish")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_TEACHER','ROLE_TEACHER_ASSISTANT')")
    public ResponseEntity<ApiResponse<List<TaskResultDTO>>> getAllTaskResultByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskResultService.getAllTaskResultByTask(taskId));
    }

    @GetMapping("/get-by-student/{studentId}")
    @Operation(summary = "Sudent orqali task resultlarni olish")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_TEACHER','ROLE_TEACHER_ASSISTANT')")
    public ResponseEntity<ApiResponse<List<TaskResultDTO>>> getAllTaskResultByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(taskResultService.getAllTaskResultByStudent(studentId));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "id orqali task resultni o'chirish")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_TEACHER','ROLE_TEACHER_ASSISTANT')")
    public ResponseEntity<ApiResponse<String>> deleteTaskResult(@PathVariable Long id) {
        return ResponseEntity.ok(taskResultService.delete(id));
    }


}
