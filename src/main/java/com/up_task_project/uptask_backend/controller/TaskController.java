package com.up_task_project.uptask_backend.controller;

import com.up_task_project.uptask_backend.dto.request.task.UpdateTaskDTO;
import com.up_task_project.uptask_backend.dto.request.task.CreateTaskDTO;
import com.up_task_project.uptask_backend.dto.request.UpdateStateTaskDTO;
import com.up_task_project.uptask_backend.dto.response.SuccessResponseDTO;
import com.up_task_project.uptask_backend.model.Task;
import com.up_task_project.uptask_backend.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/createTask")
    public ResponseEntity<Task> createTask(@RequestBody @Valid CreateTaskDTO request) {
        Task task = this.taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/getTasksByProjectId")
    public ResponseEntity<List<Task>> getTasksByProjectId(@RequestParam String projectId) {
        List<Task> taskList = this.taskService.getTasksByProjectId(projectId);
        return ResponseEntity.status(HttpStatus.OK).body(taskList);
    }

    @GetMapping("/getTaskById")
    public ResponseEntity<Task> getTaskById(@RequestParam String id, @RequestParam String projectId) {
        Task task = this.taskService.getTaskById(id, projectId);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @PutMapping("/updateTaskById")
    public ResponseEntity<Task> updateTaskById(@RequestParam String id, @RequestParam String projectId, @RequestBody @Valid UpdateTaskDTO request) {
        Task task = this.taskService.updateTaskById(id, projectId, request);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @PatchMapping("/updateStateTaskById")
    public ResponseEntity<SuccessResponseDTO> updateStateTaskById(@RequestParam String id, @RequestParam String projectId, @RequestBody @Valid UpdateStateTaskDTO status) {
        this.taskService.updateStateTaskById(id, projectId, status);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponseDTO("State task updated successfully"));
    }

    @DeleteMapping("/deleteTaskById")
    public ResponseEntity<SuccessResponseDTO> deleteTaskById(@RequestParam String id, @RequestParam String projectId) {
        this.taskService.deleteTaskById(id, projectId);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponseDTO("Task deleted successfully"));
    }

}
