package com.up_task_project.uptask_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserDTO(

    @JsonProperty("_id")
    String id,

    @JsonProperty("username")
    String username,

    @JsonProperty("email")
    String email,

    @JsonProperty("createdAt")
    LocalDateTime createdAt

) { }
