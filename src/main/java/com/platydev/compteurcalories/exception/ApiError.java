package com.platydev.compteurcalories.exception;

public record ApiError(int status, String exceptionClass, String message) {
}
