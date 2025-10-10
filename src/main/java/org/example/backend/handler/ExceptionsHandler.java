package org.example.backend.handler;

import org.example.backend.exceptions.*;
import org.example.backend.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(
                        exception = {
                                UsernameAlreadyExistsException.class,
                                EmailAlreadyExistsException.class,
                                AuthenticationException.class,
                                HeaderIsInvalidException.class,
                                UserPasswordNotMatchException.class,
                                TokenInvalidException.class,
                        })
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyExists(RuntimeException ex) {
        ErrorResponse errorResponse = ErrorResponse
                .builder().errorMessage(ex.getMessage()).build();
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }

    @ExceptionHandler(exception = {
            UsernameNotEqualsTokenException.class,
            UserNotExistsException.class,
            MessageNotFoundException.class,
            MessageCanNotBeNullException.class,
    })
    public ResponseEntity<ErrorResponse> handleUsernameIsNotEqualsToken(RuntimeException ex) {
        ErrorResponse errorResponse = ErrorResponse
                .builder().errorMessage(ex.getMessage()).build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse errorResponse = ErrorResponse
                .builder().errorMessage(ex.getMessage()).build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }
}
