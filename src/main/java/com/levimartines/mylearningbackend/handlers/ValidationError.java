package com.levimartines.mylearningbackend.handlers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ValidationError extends StandardError {

    @JsonProperty("errors")
    @Getter
    private Map<String, String> errors = new HashMap<>();

    public ValidationError(Long timestamp, Integer status, String error, String message,
                           String path) {
        super(timestamp, status, error, message, path);
    }

    public void add(String key, String message) {
        errors.put(key, message);
    }

}
