package com.up_task_project.uptask_backend.exception.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponseException(

    @JsonProperty("message")
    String message

) { }
