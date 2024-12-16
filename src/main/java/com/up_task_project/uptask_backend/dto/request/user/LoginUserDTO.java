package com.up_task_project.uptask_backend.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record LoginUserDTO(

    @JsonProperty("username")
    @NotBlank(message = "field username is required")
    String username,

    @JsonProperty("password")
    @NotBlank(message = "field password is required")
    String password

) { }
