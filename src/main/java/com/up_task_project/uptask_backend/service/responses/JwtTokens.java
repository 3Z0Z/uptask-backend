package com.up_task_project.uptask_backend.service.responses;

import lombok.Builder;

@Builder
public record JwtTokens(

    String refreshToken,
    String token

) { }
