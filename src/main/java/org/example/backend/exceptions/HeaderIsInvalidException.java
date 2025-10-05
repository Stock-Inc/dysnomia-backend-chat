package org.example.backend.exceptions;

public class HeaderIsInvalidException extends RuntimeException {
    public HeaderIsInvalidException() {
        super("Header is invalid");
    }
}
