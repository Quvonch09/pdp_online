package com.example.pdponline.mapper;

import com.example.pdponline.entity.TaskResult;
import com.example.pdponline.payload.TaskResultDTO;
import java.util.List;


public class TaskResultMapper {

    public static TaskResultDTO toTaskResultDTO(TaskResult taskResult){
        return TaskResultDTO.builder()
                .ball(taskResult.getBall())
                .TaskId(taskResult.getTask().getId())
                .studentId(taskResult.getStudent().getId())
                .id(taskResult.getId())
                .build();
    }
    public static List<TaskResultDTO> toTaskResultDTOList(List<TaskResult> taskResults){
        return taskResults.stream()
                .map(TaskResultMapper::toTaskResultDTO)
                .toList();
    }
}
