package com.levimartines.mylearningbackend.controllers;

import com.levimartines.mylearningbackend.models.vos.CoffeeOrderVO;
import com.levimartines.mylearningbackend.services.OrderService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CoffeeOrderVO coffeeOrderVO) {
        service.addToQueue(coffeeOrderVO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
