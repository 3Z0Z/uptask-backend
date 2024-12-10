package com.up_task_project.uptask_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.up_task_project.uptask_backend.model.enums.TaskStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskDTO(

    @JsonProperty("project_id")
    @NotBlank(message = "Field project_id is required")
    String projectId,

    @JsonProperty("name")
    @NotBlank(message = "Field name is required")
    String name,

    @JsonProperty("description")
    @NotBlank(message = "Field description is required")
    String description,

    @JsonProperty("status")
    @NotNull(message = "Field status is required")
    @Valid
    TaskStatus status

) { }
