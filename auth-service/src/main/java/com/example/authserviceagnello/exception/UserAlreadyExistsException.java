package com.example.authserviceagnello.exception;

public class UserAlreadyExistsException extends AuthException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
} 