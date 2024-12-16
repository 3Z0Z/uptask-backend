package com.up_task_project.uptask_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record ProjectDTO(

    @JsonProperty("projectName")
    @NotBlank(message = "Field project_name is required")
    String projectName,

    @JsonProperty("clientName")
    @NotBlank(message = "Field client_name is required")
    String clientName,

    @JsonProperty("description")
    @NotBlank(message = "Field description is required")
    String description

) { }
