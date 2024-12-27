package com.up_task_project.uptask_backend.service;

import com.up_task_project.uptask_backend.dto.request.user.LoginUserDTO;
import com.up_task_project.uptask_backend.dto.request.user.RegisterUserDTO;
import com.up_task_project.uptask_backend.dto.request.user.RequestCodeDTO;
import com.up_task_project.uptask_backend.dto.request.user.ValidateCodeDTO;
import com.up_task_project.uptask_backend.dto.request.user.ChangePasswordDTO;
import com.up_task_project.uptask_backend.dto.response.TokenJwtDTO;
import com.up_task_project.uptask_backend.dto.response.UserDTO;
import com.up_task_project.uptask_backend.service.responses.JwtTokens;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    UserDTO registerUser(RegisterUserDTO request);

    void validateEmail(ValidateCodeDTO request);

    JwtTokens login(LoginUserDTO request);

    TokenJwtDTO refreshToken(HttpServletRequest request);

    void requestCode(RequestCodeDTO request, boolean isForgotPassword);

    void validateCodeForChangePassword(ValidateCodeDTO request);

    void changePassword(ChangePasswordDTO request);

}
