package com.levimartines.mylearningbackend.controllers;

import com.levimartines.mylearningbackend.models.entities.Coffee;
import com.levimartines.mylearningbackend.services.CoffeeService;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coffees")
@RequiredArgsConstructor
public class CoffeeController {

    private final CoffeeService coffeeService;

    @GetMapping
    public ResponseEntity<List<Coffee>> findAll() {
        return ResponseEntity.ok(coffeeService.findAll());
    }

}
