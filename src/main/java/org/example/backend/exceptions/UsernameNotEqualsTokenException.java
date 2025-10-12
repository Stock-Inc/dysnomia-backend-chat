package org.example.backend.exceptions;

public class UsernameNotEqualsTokenException extends RuntimeException {
    public UsernameNotEqualsTokenException() {
        super("Username is not equal to token username");
    }
}
