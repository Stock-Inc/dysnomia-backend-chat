package org.example.backend.exceptions;

public class UserNotExistsException extends RuntimeException {
    public UserNotExistsException() {
        super("User does not exists");
    }
}
