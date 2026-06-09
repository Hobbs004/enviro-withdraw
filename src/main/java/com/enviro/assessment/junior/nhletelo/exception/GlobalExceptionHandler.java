package com.enviro.assessment.junior.nhletelo.exception;

import com.enviro.assessment.junior.nhletelo.dto.ApiResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Centralised exception handling for all REST controllers.
 * Translates domain exceptions into consistent HTTP error responses.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /** Handle business-rule violations (e.g. withdrawal limits, age restriction) */
    @ExceptionHandler(WithdrawalValidationException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleValidation(WithdrawalValidationException ex) {
        log.warn("Withdrawal validation failed: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ApiResponseDTO.error(ex.getMessage()));
    }

    /** Handle missing entity lookups */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleNotFound(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponseDTO.error(ex.getMessage()));
    }

    /** Handle @Valid annotation failures — returns field-level error map */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            errors.put(field, error.getDefaultMessage());
        });
        log.warn("Input validation failed: {}", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDTO.<Map<String, String>>builder()
                        .success(false)
                        .message("Validation failed")
                        .data(errors)
                        .build());
    }

    /** Catch-all for unexpected errors */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleGeneral(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDTO.error("An unexpected error occurred. Please try again."));
    }
}