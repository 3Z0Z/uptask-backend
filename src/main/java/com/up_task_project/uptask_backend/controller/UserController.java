package com.up_task_project.uptask_backend.controller;

import com.up_task_project.uptask_backend.dto.request.user.LoginUserDTO;
import com.up_task_project.uptask_backend.dto.request.user.RegisterUserDTO;
import com.up_task_project.uptask_backend.dto.request.user.ValidateEmailUserDTO;
import com.up_task_project.uptask_backend.dto.response.SuccessResponseDTO;
import com.up_task_project.uptask_backend.dto.response.TokenJwtDTO;
import com.up_task_project.uptask_backend.dto.response.UserDTO;
import com.up_task_project.uptask_backend.service.UserService;
import com.up_task_project.uptask_backend.service.responses.JwtTokens;
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
    public ResponseEntity<SuccessResponseDTO> validateEmail(@RequestBody @Valid ValidateEmailUserDTO request) {
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
    public ResponseEntity<TokenJwtDTO> refreshToken() {
        return null;
    }

}
