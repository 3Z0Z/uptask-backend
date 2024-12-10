package com.up_task_project.uptask_backend.service;

import com.up_task_project.uptask_backend.dto.TaskDTO;
import com.up_task_project.uptask_backend.dto.request.UpdateStateTaskDTO;
import com.up_task_project.uptask_backend.model.Task;

import java.util.List;

public interface TaskService {

    Task createTask(TaskDTO request);

    List<Task> getTasksByProjectId(String projectId);

    Task getTaskById(String id, String projectId);

    Task updateTaskById(String id, String projectId, TaskDTO request);

    void updateStateTaskById(String id, String projectId, UpdateStateTaskDTO status);

    void deleteTaskById(String id, String projectId);

}
