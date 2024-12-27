package com.up_task_project.uptask_backend.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;

public record RequestCodeDTO(

    @JsonProperty("email")
    @Email(message = "is not a valid email format")
    String email

) { }
