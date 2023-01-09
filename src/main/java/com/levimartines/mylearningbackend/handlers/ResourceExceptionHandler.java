package com.levimartines.mylearningbackend.handlers;

import com.levimartines.mylearningbackend.exceptions.NotFoundException;
import com.levimartines.mylearningbackend.exceptions.SecurityContextException;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ResourceExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> objectNotFound(NotFoundException e) {
        log.error(e.getMessage(), e);
        HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemDetail err = ProblemDetail.forStatusAndDetail(status, e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(SecurityContextException.class)
    public ResponseEntity<ProblemDetail> authenticationContextError(SecurityContextException e) {
        log.error(e.getMessage(), e);
        HttpStatus status = HttpStatus.FORBIDDEN;
        ProblemDetail err = ProblemDetail.forStatusAndDetail(status, e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> dataIntegrityViolationException(DataIntegrityViolationException e,
                                                                         HttpServletRequest request) {
        log.error(e.getMessage(), e);
        var status = HttpStatus.BAD_REQUEST;
        ProblemDetail err = ProblemDetail.forStatusAndDetail(status, e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> validationError(MethodArgumentNotValidException e,
                                                      HttpServletRequest request) {
        log.error(e.getMessage(), e);
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
    public ResponseEntity<ProblemDetail> genericError(Exception e) {
        log.error(e.getMessage(), e);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemDetail err = ProblemDetail.forStatusAndDetail(status, e.getMessage());
        return ResponseEntity.status(status).body(err);
    }

}
