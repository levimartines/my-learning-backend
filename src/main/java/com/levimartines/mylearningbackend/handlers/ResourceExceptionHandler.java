package com.levimartines.mylearningbackend.handlers;

import com.levimartines.mylearningbackend.exceptions.SecurityContextException;
import com.levimartines.mylearningbackend.exceptions.NotFoundException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
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


}
