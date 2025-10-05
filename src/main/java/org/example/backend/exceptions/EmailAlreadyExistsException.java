package org.example.backend.exceptions;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Registration failed");
    }
}
