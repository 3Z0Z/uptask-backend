package com.up_task_project.uptask_backend.controller;

import com.up_task_project.uptask_backend.dto.request.user.LoginUserDTO;
import com.up_task_project.uptask_backend.dto.request.user.RegisterUserDTO;
import com.up_task_project.uptask_backend.dto.request.user.RequestCodeDTO;
import com.up_task_project.uptask_backend.dto.request.user.ValidateCodeDTO;
import com.up_task_project.uptask_backend.dto.request.user.ChangePasswordDTO;
import com.up_task_project.uptask_backend.dto.response.SuccessResponseDTO;
import com.up_task_project.uptask_backend.dto.response.TokenJwtDTO;
import com.up_task_project.uptask_backend.dto.response.UserDTO;
import com.up_task_project.uptask_backend.service.UserService;
import com.up_task_project.uptask_backend.service.responses.JwtTokens;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody @Valid RegisterUserDTO request) {
        UserDTO newUser = this.userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PostMapping("/validateEmail")
    public ResponseEntity<SuccessResponseDTO> validateEmail(@RequestBody @Valid ValidateCodeDTO request) {
        this.userService.validateEmail(request);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponseDTO("Email confirmed"));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenJwtDTO> login(@RequestBody @Valid LoginUserDTO request) {
        JwtTokens tokens = this.userService.login(request);
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshTokenUpTask", tokens.refreshToken())
                .httpOnly(true)
                //.secure(true)
                .path("/")
                .maxAge(30 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();
        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
            .body(new TokenJwtDTO(tokens.token()));
    }

    @PutMapping("/refresh-token")
    public ResponseEntity<TokenJwtDTO> refreshToken(HttpServletRequest request) {
        TokenJwtDTO newJwtToken = this.userService.refreshToken(request);
        return ResponseEntity.status(HttpStatus.OK).body(newJwtToken);
    }

    @PostMapping("/request-code")
    public ResponseEntity<SuccessResponseDTO> requestCode(@RequestBody @Valid RequestCodeDTO request) {
        this.userService.requestCode(request, false);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<SuccessResponseDTO> forgotPassword(@RequestBody @Valid RequestCodeDTO request) {
        this.userService.requestCode(request, true);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponseDTO("Email send successfully"));
    }

    @PostMapping("/validate-code-change-password")
    public ResponseEntity<SuccessResponseDTO> validateCode(@RequestBody @Valid ValidateCodeDTO request) {
        this.userService.validateCodeForChangePassword(request);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponseDTO("Code validated successfully, proceed to change your password"));
    }

    @PutMapping("/change-password")
    public ResponseEntity<SuccessResponseDTO> changePassword(@RequestBody @Valid ChangePasswordDTO request) {
        this.userService.changePassword(request);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponseDTO("Password updated successfully"));
    }

}
