package com.levimartines.mylearningbackend.repositories;

import com.levimartines.mylearningbackend.models.entities.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);
}
