package com.up_task_project.uptask_backend.repository;

import com.up_task_project.uptask_backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    boolean existsByEmailOrUsername(String email, String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}
