package com.up_task_project.uptask_backend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SuccessResponseDTO(

    @JsonProperty("message")
    String message

) { }
