package com.up_task_project.uptask_backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "token")
public class Token {

    @Id
    private String _id;

    @Indexed(unique = true)
    private String userId;

    private String token;

    @CreatedDate
    @Indexed(expireAfterSeconds = 600)
    private LocalDateTime createdAt;

}
