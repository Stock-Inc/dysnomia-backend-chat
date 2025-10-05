package org.example.backend.exceptions;

public class UsernameNotEqualsToken extends RuntimeException {
    public UsernameNotEqualsToken() {
        super("Username is not equal to token username");
    }
}
