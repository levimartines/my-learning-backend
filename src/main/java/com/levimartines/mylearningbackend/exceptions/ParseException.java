package com.levimartines.mylearningbackend.exceptions;

public class ParseException extends RuntimeException {

    public ParseException(Exception e) {
        super(e);
    }
}
