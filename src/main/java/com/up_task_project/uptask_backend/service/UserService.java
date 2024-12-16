package com.up_task_project.uptask_backend.service;

import com.up_task_project.uptask_backend.dto.request.user.LoginUserDTO;
import com.up_task_project.uptask_backend.dto.request.user.RegisterUserDTO;
import com.up_task_project.uptask_backend.dto.request.user.ValidateEmailUserDTO;
import com.up_task_project.uptask_backend.dto.response.UserDTO;
import com.up_task_project.uptask_backend.service.responses.JwtTokens;

public interface UserService {

    UserDTO registerUser(RegisterUserDTO request);

    void validateEmail(ValidateEmailUserDTO request);

    JwtTokens login(LoginUserDTO request);

}
