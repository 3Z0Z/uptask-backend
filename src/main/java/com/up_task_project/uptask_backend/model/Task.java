package com.up_task_project.uptask_backend.model;

import com.up_task_project.uptask_backend.model.enums.TaskStatus;
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
@Document(collection = "task")
public class Task {

    @Id
    private String _id;

    private String projectId;

    private String name;

    private String description;

    private TaskStatus status;

    @CreatedDate
    private LocalDateTime createAt;

}
