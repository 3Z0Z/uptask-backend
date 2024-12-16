package com.up_task_project.uptask_backend.exception.controller;

import com.up_task_project.uptask_backend.exception.exceptions.*;
import com.up_task_project.uptask_backend.exception.response.ResponseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<ResponseException> handleUserRegistrationException(ProjectNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseException(e.getMessage()));
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ResponseException> handleTaskNotFoundException(TaskNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseException(e.getMessage()));
    }

    @ExceptionHandler(UnathorizeActionException.class)
    public ResponseEntity<ResponseException> handleUnathorizeActionException(UnathorizeActionException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseException(e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseException> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseException(e.getMessage()));
    }

    @ExceptionHandler(UserAlreadyRegisterException.class)
    public ResponseEntity<ResponseException> handleUserAlreadyRegisterException(UserAlreadyRegisterException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseException(e.getMessage()));
    }

    @ExceptionHandler(SendEmailException.class)
    public ResponseEntity<ResponseException> handleSendEmailException(SendEmailException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseException(e.getMessage()));
    }

    @ExceptionHandler(TokenNotFoundOrExpiredException.class)
    public ResponseEntity<ResponseException> handleTokenNotFoundOrExpiredException(TokenNotFoundOrExpiredException e) {
        return ResponseEntity.status(HttpStatus.UPGRADE_REQUIRED).body(new ResponseException(e.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ResponseException> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseException(e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseException> handleHttpMessageNotReadableException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseException("Status send must be on of the allowing options"));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Missing required parameter");
        response.put("parameter", ex.getParameterName());
        response.put("message", String.format("The '%s' parameter is required and was not provided", ex.getParameterName()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
