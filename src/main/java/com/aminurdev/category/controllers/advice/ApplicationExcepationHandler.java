package com.aminurdev.category.controllers.advice;

import com.aminurdev.category.response.ErrorResponse;
import jakarta.validation.ValidationException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExcepationHandler {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception)
    {
        Map<String, Object> errors = new  HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setErrors(errors);
        errorResponse.setMessage("Validation error");
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
