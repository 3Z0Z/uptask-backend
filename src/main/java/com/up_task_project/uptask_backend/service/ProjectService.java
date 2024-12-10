package com.up_task_project.uptask_backend.service;

import com.up_task_project.uptask_backend.dto.ProjectDTO;
import com.up_task_project.uptask_backend.model.Project;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProjectService {

    ResponseEntity<?> createProject(ProjectDTO request);

    ResponseEntity<List<Project>> getProjects();

    ResponseEntity<Project> getProjectById(String id);

    ResponseEntity<Project> updateProjectById(String id, ProjectDTO request);

    ResponseEntity<?> deleteProjectById(String id);

}
