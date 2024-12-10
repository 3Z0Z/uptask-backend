package com.up_task_project.uptask_backend.service.impl;

import com.up_task_project.uptask_backend.dto.ProjectDTO;
import com.up_task_project.uptask_backend.exception.exceptions.ProjectNotFoundException;
import com.up_task_project.uptask_backend.model.Project;
import com.up_task_project.uptask_backend.repository.ProjectRepository;
import com.up_task_project.uptask_backend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public ResponseEntity<?> createProject(ProjectDTO request) {
        Project newProject = Project.builder()
                .projectName(request.projectName())
                .clientName(request.clientName())
                .description(request.description())
                .build();
        newProject = this.projectRepository.save(newProject);
        log.info("Project created: {}, {}", newProject.get_id(), newProject.getProjectName());
        return ResponseEntity.status(HttpStatus.CREATED).body(newProject);
    }

    @Override
    public ResponseEntity<List<Project>> getProjects() {
        List<Project> projects = this.projectRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(projects);
    }

    @Override
    public ResponseEntity<Project> getProjectById(String id) {
        Project project = this.findProjectById(id);
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }

    @Override
    public ResponseEntity<Project> updateProjectById(String id, ProjectDTO request) {
        Project project = this.findProjectById(id);
        project.setProjectName(request.projectName());
        project.setClientName(request.clientName());
        project.setDescription(request.description());
        project = this.projectRepository.save(project);
        return ResponseEntity.status(HttpStatus.OK).body(project);
    }

    @Override
    public ResponseEntity<?> deleteProjectById(String id) {
        Project project = this.findProjectById(id);
        this.projectRepository.deleteById(project.get_id());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private Project findProjectById(String id) {
        return this.projectRepository.findById(id)
            .orElseThrow(() -> new ProjectNotFoundException("Project not found with id: " + id));
    }

}
