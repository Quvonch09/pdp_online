package com.example.pdponline.service;

import com.example.pdponline.entity.File;
import com.example.pdponline.entity.Lesson;
import com.example.pdponline.entity.Task;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.payload.TaskDTO;
import com.example.pdponline.payload.req.TaskReq;
import com.example.pdponline.repository.FileRepository;
import com.example.pdponline.repository.LessonRepository;
import com.example.pdponline.repository.TaskRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final LessonRepository lessonRepository;
    private final FileRepository fileRepository;

    public ApiResponse<String> saveTask(TaskReq taskreq) {
        Lesson lesson = lessonRepository.findById(taskreq.getLessonId()).orElseThrow(() ->
                RestException.restThrow(ResponseError.NOTFOUND("Lesson")));



        Task task = Task.builder()
                .title(taskreq.getTitle())
                .description(taskreq.getDescription())
                .lesson(lesson)
                .attachments(taskreq.getFilesUrl())
                .startTime(LocalTime.parse( taskreq.getStartTime()))
                .endTime(LocalTime.parse( taskreq.getEndTime()))
                .build();

        taskRepository.save(task);
        return ApiResponse.successResponse("Task saved");
    }


    public ApiResponse<String> updateTask(Long id, @Valid TaskReq taskReq) {
        Task task = taskRepository.findById(id).orElseThrow(() ->
                RestException.restThrow(ResponseError.NOTFOUND("Task")));

        if (task.getTitle() != null && !task.getTitle().equals(taskReq.getTitle())) {
            task.setTitle(taskReq.getTitle());
        }

        task.setDescription(taskReq.getDescription());
        task.setStartTime(LocalTime.parse(taskReq.getStartTime()));
        task.setEndTime(LocalTime.parse(taskReq.getEndTime()));
        task.setAttachments(taskReq.getFilesUrl() != null ? taskReq.getFilesUrl() : null);
        taskRepository.save(task);
        return null;
    }

//    private List<File> getFiles(List<Long> filesId) {
//        List<File> files = new ArrayList<>();
//        if (!filesId.isEmpty()) {
//            filesId.forEach(fileId -> {
//                files.add(fileRepository.findById(fileId).get());
//            });
//        }
//        return files;
//    }

    public ApiResponse<TaskDTO> getTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() ->
                RestException.restThrow(ResponseError.NOTFOUND("Task")));
        List<String> urls = taskRepository.findAllByTaskUrls(id);

        return ApiResponse.successResponse(parseToTaskDTO(task, urls));
    }


    public ApiResponse<List<TaskDTO>> getByLessonId(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() ->
                RestException.restThrow(ResponseError.NOTFOUND("Lesson")));
        List<Task> tasks = taskRepository.findAllByLessonId(lesson.getId());
        List<TaskDTO> taskDTOs = tasks.stream().map(
                task -> parseToTaskDTO(task, taskRepository.findAllByTaskUrls(task.getId()))
        ).toList();
        return ApiResponse.successResponse(taskDTOs);
    }

    private TaskDTO parseToTaskDTO(Task task, List<String> urls) {

        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .lessonId(task.getLesson().getId())
                .attachments(urls)
                .starTime(task.getStartTime())
                .ednTime(task.getEndTime())
                .build();
    }

    public ApiResponse<String> deleteTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() ->
                RestException.restThrow(ResponseError.NOTFOUND("Task")));
        task.setDeleted(true);
        taskRepository.save(task);
        return ApiResponse.successResponse("Task deleted successfully");
    }
}
