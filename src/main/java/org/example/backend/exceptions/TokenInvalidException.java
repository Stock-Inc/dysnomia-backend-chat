package org.example.backend.exceptions;

public class TokenInvalidException extends RuntimeException {
    public TokenInvalidException() {
        super("Token is invalid");
    }
}
