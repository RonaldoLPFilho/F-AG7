package com.example.authserviceagnello.exception;

import com.example.authserviceagnello.dto.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<APIResponse<Void>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(APIResponse.error(ex.getMessage(), HttpStatus.CONFLICT));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<APIResponse<Void>> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(APIResponse.error(ex.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<APIResponse<Void>> handleInvalidTokenException(InvalidTokenException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(APIResponse.error(ex.getMessage(), HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<APIResponse<Void>> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(APIResponse.error("Credenciais inválidas", HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Void>> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIResponse.error("Erro interno do servidor: " + ex.getMessage(), 
                        HttpStatus.INTERNAL_SERVER_ERROR));
    }
} 