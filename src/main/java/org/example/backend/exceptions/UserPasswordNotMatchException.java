package org.example.backend.exceptions;

public class UserPasswordNotMatchException extends RuntimeException {
    public UserPasswordNotMatchException() {
        super("Old Password Not Match to New Password");
    }
}
