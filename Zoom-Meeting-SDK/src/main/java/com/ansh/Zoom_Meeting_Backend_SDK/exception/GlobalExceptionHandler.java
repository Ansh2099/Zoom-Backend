package com.ansh.Zoom_Meeting_Backend_SDK.exception;

import com.ansh.Zoom_Meeting_Backend_SDK.dto.ValidationError;
import com.ansh.Zoom_Meeting_Backend_SDK.validation.MeetingNumberAndRoleRequired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<ValidationError>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        
        List<ValidationError> errors = new ArrayList<>();
        
        // Handle field validation errors
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            String property = fieldError.getField();
            String reason = fieldError.getDefaultMessage();
            errors.add(new ValidationError(property, reason));
        }
        
        // Handle custom validation errors (like MeetingNumberAndRoleRequired)
        if (ex.getBindingResult().getGlobalErrors().size() > 0) {
            for (var globalError : ex.getBindingResult().getGlobalErrors()) {
                if (globalError.getCode().equals(MeetingNumberAndRoleRequired.class.getSimpleName())) {
                    errors.add(new ValidationError("meetingNumber", globalError.getDefaultMessage()));
                    errors.add(new ValidationError("role", globalError.getDefaultMessage()));
                }
            }
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("errors", errors));
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, List<ValidationError>>> handleConstraintViolationException(
            ConstraintViolationException ex) {
        
        List<ValidationError> errors = new ArrayList<>();
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        
        for (ConstraintViolation<?> violation : violations) {
            String property = violation.getPropertyPath().toString();
            String reason = violation.getMessage();
            errors.add(new ValidationError(property, reason));
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("errors", errors));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, List<ValidationError>>> handleGenericException(Exception ex) {
        List<ValidationError> errors = new ArrayList<>();
        errors.add(new ValidationError("general", ex.getMessage()));
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("errors", errors));
    }
} 