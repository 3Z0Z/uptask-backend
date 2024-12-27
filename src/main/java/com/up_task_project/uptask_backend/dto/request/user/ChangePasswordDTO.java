package com.up_task_project.uptask_backend.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ChangePasswordDTO(

    @JsonProperty("userId")
    @NotBlank(message = "userId field is required")
    String userId,

    @JsonProperty("password")
    @NotBlank(message = "password field is required")
    @Pattern(message = "must have at least 10 and 50 characters, a number and a special sing", regexp = "^(?=.*\\d)(?=.*[-_*?!@/().#=])[A-Za-z\\d-_*?!@/().#=]{10,50}$")
    String password

) { }
