package com.example.pdponline.service;

import com.example.pdponline.entity.Task;
import com.example.pdponline.entity.TaskResult;
import com.example.pdponline.entity.enums.Role;
import com.example.pdponline.mapper.TaskResultMapper;
import com.example.pdponline.payload.TaskResultDTO;
import com.example.pdponline.entity.User;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.payload.ApiResponse;
import com.example.pdponline.payload.ResponseError;
import com.example.pdponline.repository.TaskRepository;
import com.example.pdponline.repository.TaskResultRepository;
import com.example.pdponline.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskResultService {
    private final TaskResultRepository taskResultRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;


    public ApiResponse<String> createTaskResult(TaskResultDTO taskResultDTO) {
        TaskResult result = getBuild(taskResultDTO);
        try {
            taskResultRepository.save(result);
        }catch (Exception e){
            throw RestException.restThrow(ResponseError.ALREADY_EXIST("Task Result"));
        }

        return ApiResponse.successResponse("Task Result saved");
    }


    public ApiResponse<String> updateTaskResult(Long taskResultId, TaskResultDTO taskResultDTO) {
        TaskResult taskResult = taskResultRepository.findById(taskResultId).orElseThrow(() ->
                RestException.restThrow(ResponseError.NOTFOUND("Task Result")));
        taskResult.setBall(taskResultDTO.ball());
        taskResult.setSuccess(taskResultDTO.ball() > 3);
        taskResult.setStudent(getStudent(taskResultDTO.studentId()));
        taskResult.setTask(getTask(taskResultDTO.TaskId()));
        taskResultRepository.save(taskResult);

        return ApiResponse.successResponse("Task Result updated");
    }

    public ApiResponse<TaskResultDTO> getTaskResult(Long id) {
        TaskResult taskResult = taskResultRepository.findById(id).orElseThrow(() ->
                RestException.restThrow(ResponseError.NOTFOUND("Task Result")));
        return ApiResponse.successResponse(TaskResultMapper.toTaskResultDTO(taskResult));
    }

    private TaskResult getBuild(TaskResultDTO taskResultDTO) {
        User student = getStudent(taskResultDTO.studentId());
        Task task = getTask(taskResultDTO.TaskId());
        return TaskResult.builder()
                .student(student)
                .task(task)
                .ball(taskResultDTO.ball())
                .success(taskResultDTO.ball() > 6)
                .build();
    }

    private Task getTask(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(
                () -> RestException.restThrow(ResponseError.NOTFOUND("Task"))
        );
    }

    private User getStudent(Long taskId) {
        return userRepository.findByIdAndRole(taskId, Role.ROLE_STUDENT).orElseThrow(
                () -> RestException.restThrow(ResponseError.NOTFOUND("Student"))
        );
    }


    public ApiResponse<List<TaskResultDTO>> getAllTaskResultByTask(Long taskId) {
        List<TaskResult> byTaskId = taskResultRepository.findByTask_Id(taskId);
        if (byTaskId.isEmpty()) {
            throw RestException.restThrow(ResponseError.NOTFOUND("But task bo'yicha task results"));
        }
        return ApiResponse.successResponse(TaskResultMapper.toTaskResultDTOList(byTaskId));
    }

    public ApiResponse<List<TaskResultDTO>> getAllTaskResultByStudent(Long studentId) {
        List<TaskResult> byStudentId = taskResultRepository.findByStudent_Id(studentId);
        if (byStudentId.isEmpty()) {
            throw RestException.restThrow(ResponseError.NOTFOUND("Student bo'yicha task results"));
        }
        return ApiResponse.successResponse(TaskResultMapper.toTaskResultDTOList(byStudentId));
    }

    public ApiResponse<String> delete(Long id) {
        TaskResult taskResult = taskResultRepository.findById(id).orElseThrow(
                () -> RestException.restThrow(ResponseError.NOTFOUND("Task Result"))
        );
        taskResultRepository.delete(taskResult);
        return ApiResponse.successResponse("Task Result deleted");
    }
}
