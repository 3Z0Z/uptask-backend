package com.up_task_project.uptask_backend.repository;

import com.up_task_project.uptask_backend.model.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends MongoRepository<Session, String> {

    Optional<Session> findByUserId(String userId);

}
