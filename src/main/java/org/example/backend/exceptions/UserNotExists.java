package org.example.backend.exceptions;

public class UserNotExists extends RuntimeException {
    public UserNotExists() {
        super("User does not exists");
    }
}
