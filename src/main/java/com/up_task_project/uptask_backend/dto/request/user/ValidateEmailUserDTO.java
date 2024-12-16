package com.up_task_project.uptask_backend.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record ValidateEmailUserDTO(

    @JsonProperty("token")
    @NotBlank(message = "field token is required")
    String token,

    @JsonProperty("userId")
    @NotBlank(message = "field userId is required")
    String userId

) { }
