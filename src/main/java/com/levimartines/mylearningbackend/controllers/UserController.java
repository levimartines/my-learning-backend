package com.levimartines.mylearningbackend.controllers;

import com.levimartines.mylearningbackend.dtos.UserDto;
import com.levimartines.mylearningbackend.services.UserService;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<Void> create(@RequestBody @Valid UserDto body) {
        service.create(service.fromDto(body));
        return ResponseEntity.ok().build();
    }
}
