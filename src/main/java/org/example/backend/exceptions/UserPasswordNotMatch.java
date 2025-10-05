package org.example.backend.exceptions;

public class UserPasswordNotMatch extends RuntimeException {
    public UserPasswordNotMatch() {
        super("Old Password Not Match to New Password");
    }
}
