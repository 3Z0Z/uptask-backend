package com.up_task_project.uptask_backend.controller;

import com.up_task_project.uptask_backend.dto.ProjectDTO;
import com.up_task_project.uptask_backend.model.Project;
import com.up_task_project.uptask_backend.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/createProject")
    public ResponseEntity<?> createProject(@RequestBody @Valid ProjectDTO request) {
        return this.projectService.createProject(request);
    }

    @GetMapping("/getProjects")
    public ResponseEntity<List<Project>> getProjects() {
        return this.projectService.getProjects();
    }

    @GetMapping("/getProjectById")
    public ResponseEntity<Project> getProjectById(@RequestParam String id) {
        return this.projectService.getProjectById(id);
    }

    @PutMapping("/updateProjectById")
    public ResponseEntity<Project> updateProjectById(@RequestParam String id, @RequestBody @Valid ProjectDTO request) {
        return this.projectService.updateProjectById(id, request);
    }

    @DeleteMapping("/deleteProjectById")
    public ResponseEntity<?> deleteProjectById(@RequestParam String id) {
        return this.projectService.deleteProjectById(id);
    }

}
