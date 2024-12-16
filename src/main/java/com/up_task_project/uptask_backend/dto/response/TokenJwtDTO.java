package com.up_task_project.uptask_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenJwtDTO(

    @JsonProperty("token")
    String token

) { }
