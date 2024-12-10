package com.up_task_project.uptask_backend.service.impl;

import com.up_task_project.uptask_backend.dto.TaskDTO;
import com.up_task_project.uptask_backend.dto.request.UpdateStateTaskDTO;
import com.up_task_project.uptask_backend.exception.exceptions.ProjectNotFoundException;
import com.up_task_project.uptask_backend.exception.exceptions.TaskNotFoundException;
import com.up_task_project.uptask_backend.model.Task;
import com.up_task_project.uptask_backend.repository.ProjectRepository;
import com.up_task_project.uptask_backend.repository.TaskRepository;
import com.up_task_project.uptask_backend.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    @Override
    public Task createTask(TaskDTO request) {
        this.validateProjectExistence(request.projectId());
        Task newTask = Task.builder()
            .projectId(request.projectId())
            .name(request.name())
            .description(request.description())
            .status(request.status())
            .build();
        newTask = this.taskRepository.save(newTask);
        return newTask;
    }

    @Override
    public List<Task> getTasksByProjectId(String projectId) {
        this.validateProjectExistence(projectId);
        return this.taskRepository.findByProjectId(projectId);
    }

    @Override
    public Task getTaskById(String id, String projectId) {
        return this.findTaskByIdAndValidateProject(id, projectId);
    }

    @Override
    public Task updateTaskById(String id, String projectId, TaskDTO request) {
        Task task = this.findTaskByIdAndValidateProject(id, projectId);
        task.setName(request.name());
        task.setDescription(request.description());
        task.setStatus(request.status());
        task = this.taskRepository.save(task);
        return task;
    }

    @Override
    public void updateStateTaskById(String id, String projectId, UpdateStateTaskDTO status) {
        Task task = this.findTaskByIdAndValidateProject(id, projectId);
        task.setStatus(status.status());
        this.taskRepository.save(task);
    }

    @Override
    public void deleteTaskById(String id, String projectId) {
        Task task = this.findTaskByIdAndValidateProject(id, projectId);
        this.taskRepository.delete(task);
    }

    private Task findTaskByIdAndValidateProject(String taskId, String projectId) {
        return this.taskRepository.findById(taskId)
            .filter(task -> task.getProjectId().equals(projectId))
            .orElseThrow(() -> new TaskNotFoundException("Task not found in the specified project: " + taskId));
    }

    private void validateProjectExistence(String projectId) {
        this.projectRepository.findById(projectId)
            .orElseThrow(() -> new ProjectNotFoundException("The project id does not exist"));
    }

}
