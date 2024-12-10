package com.up_task_project.uptask_backend.repository;

import com.up_task_project.uptask_backend.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends MongoRepository<Task, String> {

    List<Task> findByProjectId(String projectId);

}
