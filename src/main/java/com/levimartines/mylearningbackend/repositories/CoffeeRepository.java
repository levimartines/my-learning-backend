package com.levimartines.mylearningbackend.repositories;

import com.levimartines.mylearningbackend.models.entities.Coffee;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {
}
