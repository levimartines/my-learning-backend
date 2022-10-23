package com.levimartines.mylearningbackend.exceptions;

public class SqsException extends RuntimeException {

    public SqsException(Exception e) {
        super(e);
    }
}
