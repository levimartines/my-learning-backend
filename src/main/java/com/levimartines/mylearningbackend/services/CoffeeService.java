package com.levimartines.mylearningbackend.services;

import com.levimartines.mylearningbackend.models.entities.Coffee;
import com.levimartines.mylearningbackend.repositories.CoffeeRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CoffeeService {

    private final CoffeeRepository repository;

    public List<Coffee> findAll() {
        return repository.findAll();
    }
}

