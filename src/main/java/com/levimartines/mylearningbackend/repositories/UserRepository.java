package com.levimartines.mylearningbackend.repositories;

import com.levimartines.mylearningbackend.models.entities.User;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    @Cacheable(value = "users", unless = "#result == null")
    Optional<User> findByEmailIgnoreCase(String email);
}
