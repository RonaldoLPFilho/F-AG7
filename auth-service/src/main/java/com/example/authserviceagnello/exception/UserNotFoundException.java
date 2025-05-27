package com.example.authserviceagnello.exception;

public class UserNotFoundException extends AuthException {
    public UserNotFoundException(String message) {
        super(message);
    }
} 