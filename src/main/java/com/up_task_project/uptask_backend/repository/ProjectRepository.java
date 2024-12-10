package com.up_task_project.uptask_backend.repository;

import com.up_task_project.uptask_backend.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {
}
