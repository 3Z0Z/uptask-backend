package com.up_task_project.uptask_backend.dto.request.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record UpdateTaskDTO(

    @JsonProperty("name")
    @NotBlank(message = "Field name is required")
    String name,

    @JsonProperty("description")
    @NotBlank(message = "Field description is required")
    String description

) { }
