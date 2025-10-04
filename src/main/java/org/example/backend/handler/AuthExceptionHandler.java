package org.example.backend.handler;

import org.example.backend.exceptions.UsernameAlreadyExistsException;
import org.example.backend.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity handleUsernameAlreadyExists(UsernameAlreadyExistsException ex) {
        ErrorResponse errorResponse = ErrorResponse
                .builder().errorMessage(ex.getMessage()).build();
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }
}
