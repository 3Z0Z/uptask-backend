package com.up_task_project.uptask_backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "session")
public class Session {

    @Id
    private String _id;

    private String userId;

    private String refreshToken;

    private boolean refreshTokenRevoked;

    @CreatedDate
    private LocalDateTime createAt;

}
