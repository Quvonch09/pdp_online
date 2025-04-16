package com.example.pdponline.controller;

import com.example.pdponline.entity.Task;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.TaskDTO;
import com.example.pdponline.payload.req.TaskReq;
import com.example.pdponline.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/save")
    @Operation(summary = "Task qo'shish")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_TEACHER')")
    public ResponseEntity<ApiResponse<String>> saveTask(@Valid @RequestBody TaskReq taskReq) {
        return ResponseEntity.ok(taskService.saveTask(taskReq));
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_TEACHER')")
    public ResponseEntity<ApiResponse<String>> updateTask(@Valid @RequestBody TaskReq taskReq, @PathVariable Long id) {
        return ResponseEntity.ok(taskService.updateTask(id, taskReq));
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "id buyicha taskni olish")
    public ResponseEntity<ApiResponse<TaskDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTask(id));
    }
}
