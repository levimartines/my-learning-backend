package com.levimartines.mylearningbackend.repositories;

import com.levimartines.mylearningbackend.models.entities.User;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Cacheable(value = "users", unless = "#result == null", key = "#email")
    @Transactional(readOnly = true)
    Optional<User> findByEmailIgnoreCase(String email);
}
