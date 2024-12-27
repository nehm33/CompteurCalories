package com.platydev.compteurcalories.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
