package com.up_task_project.uptask_backend.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.up_task_project.uptask_backend.model.enums.TaskStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UpdateStateTaskDTO(

    @JsonProperty("state")
    @NotNull(message = "Field status is required")
    @Valid
    TaskStatus status

) { }
