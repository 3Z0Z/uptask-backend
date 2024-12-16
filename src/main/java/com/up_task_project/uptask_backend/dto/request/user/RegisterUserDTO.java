package com.up_task_project.uptask_backend.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterUserDTO(

    @JsonProperty("username")
    @NotBlank(message = "field username is required")
    @Pattern(message = "must have between 2 and 20 characters and no special sing", regexp = "^[a-zA-Z*\\d]{2,20}$")
    String username,

    @JsonProperty("email")
    @NotBlank(message = "field username is required")
    @Email(message = "invalid email format")
    String email,

    @JsonProperty("password")
    @NotBlank(message = "field username is required")
    @Pattern(message = "must have at least 10 and 50 characters, a number and a special sing", regexp = "^(?=.*\\d)(?=.*[-_*?!@/().#=])[A-Za-z\\d-_*?!@/().#=]{10,50}$")
    String password

) { }
