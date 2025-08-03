package com.platydev.compteurcalories.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError)err).getField();
            String message = err.getDefaultMessage();
            response.put(fieldName, message);
        });
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> unauthorizedHandler(UsernameNotFoundException e) {
        LOGGER.warn("Catching exception : ", e);
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED.value(), e.getClass().getName(), e.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> internalServerErrorHandler(RuntimeException e) {
        LOGGER.warn("Catching exception : ", e);
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getClass().getName(), e.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ApiException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ApiError> badRequestHandler(Exception e) {
        LOGGER.warn("Catching exception : ", e);
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST.value(), e.getClass().getName(), e.getLocalizedMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ApiError> notFoundHandler(Exception e) {
        LOGGER.warn("Catching exception : ", e);
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND.value(), e.getClass().getName(), e.getLocalizedMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<ApiError> forbiddenHandler(Exception e) {
        LOGGER.warn("Catching exception : ", e);
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN.value(), e.getClass().getName(), e.getLocalizedMessage());
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }
}
