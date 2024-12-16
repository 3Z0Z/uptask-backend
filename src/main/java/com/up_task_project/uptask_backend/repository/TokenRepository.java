package com.up_task_project.uptask_backend.repository;

import com.up_task_project.uptask_backend.model.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {

    Optional<Token> findByUserId(String userId);

    boolean existsByUserId(String userId);

}
