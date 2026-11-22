package com.everrent.global.exception.temp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class TempGlobalExceptionHandler {

    @ExceptionHandler(TempCustomException.class)
    public ResponseEntity<Map<String, String>> handleCustomException(TempCustomException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getTempErrorCode().getMessage());
        return ResponseEntity.status(e.getTempErrorCode().getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> response = new HashMap<>();
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        response.put("message", errorMessage);
        return ResponseEntity.status(TempErrorCode.INVALID_INPUT_VALUE.getStatus()).body(response);
    }
}