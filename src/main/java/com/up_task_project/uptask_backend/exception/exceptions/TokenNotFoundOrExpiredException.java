package com.up_task_project.uptask_backend.exception.exceptions;

public class TokenNotFoundOrExpiredException extends RuntimeException {

    public TokenNotFoundOrExpiredException(String message) {
        super(message);
    }

}
