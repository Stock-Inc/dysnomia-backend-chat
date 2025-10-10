package org.example.backend.exceptions;

public class MessageCanNotBeNullException extends RuntimeException {
    public MessageCanNotBeNullException(String message) {
        super(message);
    }
}
