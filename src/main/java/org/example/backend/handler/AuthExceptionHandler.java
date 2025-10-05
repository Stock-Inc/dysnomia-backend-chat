package org.example.backend.handler;

import org.example.backend.exceptions.EmailAlreadyExistsException;
import org.example.backend.exceptions.HeaderIsInvalidException;
import org.example.backend.exceptions.UsernameAlreadyExistsException;
import org.example.backend.exceptions.UsernameNotEqualsToken;
import org.example.backend.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler({UsernameAlreadyExistsException.class,
            EmailAlreadyExistsException.class})
    public ResponseEntity handleUsernameAlreadyExists(UsernameAlreadyExistsException ex) {
        ErrorResponse errorResponse = ErrorResponse
                .builder().errorMessage(ex.getMessage()).build();
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex) {
        ErrorResponse errorResponse = ErrorResponse
                .builder().errorMessage("incorrect data").build();
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }

    @ExceptionHandler(UsernameNotEqualsToken.class)
    public ResponseEntity<?> handleUsernameIsNotEqualsToken(UsernameNotEqualsToken ex) {
        ErrorResponse errorResponse = ErrorResponse
                .builder().errorMessage("Invalid username").build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(HeaderIsInvalidException.class)
    public ResponseEntity<?> handleHeaderIsInvalidException(HeaderIsInvalidException ex) {
        ErrorResponse errorResponse = ErrorResponse
                .builder().errorMessage("Invalid header").build();
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }
}
