package com.up_task_project.uptask_backend.service;

import com.up_task_project.uptask_backend.dto.request.task.UpdateTaskDTO;
import com.up_task_project.uptask_backend.dto.request.task.CreateTaskDTO;
import com.up_task_project.uptask_backend.dto.request.UpdateStateTaskDTO;
import com.up_task_project.uptask_backend.model.Task;

import java.util.List;

public interface TaskService {

    Task createTask(CreateTaskDTO request);

    List<Task> getTasksByProjectId(String projectId);

    Task getTaskById(String id, String projectId);

    Task updateTaskById(String id, String projectId, UpdateTaskDTO request);

    void updateStateTaskById(String id, String projectId, UpdateStateTaskDTO status);

    void deleteTaskById(String id, String projectId);

}
