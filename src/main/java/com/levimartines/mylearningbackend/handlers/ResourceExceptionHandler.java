package com.levimartines.mylearningbackend.handlers;

import com.levimartines.mylearningbackend.exceptions.NotFoundException;
import com.levimartines.mylearningbackend.exceptions.SecurityContextException;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ResourceExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(NotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(System.currentTimeMillis(),
            status.value(), status.getReasonPhrase(),
            e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(SecurityContextException.class)
    public ResponseEntity<StandardError> authenticationContextError(SecurityContextException e,
                                                                    HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        StandardError err = new StandardError(System.currentTimeMillis(),
            status.value(), status.getReasonPhrase(),
            e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<StandardError> dataIntegrityViolationException(DataIntegrityViolationException e,
                                                                         HttpServletRequest request) {
        var status = HttpStatus.BAD_REQUEST;
        StandardError err = new StandardError(System.currentTimeMillis(),
            status.value(), "Constraint violation",
            e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> validationError(MethodArgumentNotValidException e,
                                                      HttpServletRequest request) {
        var status = HttpStatus.BAD_REQUEST;
        ValidationError err = new ValidationError(System.currentTimeMillis(),
            status.value(), "Validation error",
            "Invalid input", request.getRequestURI());
        for (FieldError fieldErr : e.getBindingResult().getFieldErrors()) {
            err.add(fieldErr.getField(), fieldErr.getDefaultMessage());
        }
        return ResponseEntity.status(status).body(err);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardError> genericError(Exception e,
                                                                    HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        StandardError err = new StandardError(System.currentTimeMillis(),
            status.value(), status.getReasonPhrase(),
            e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

}
