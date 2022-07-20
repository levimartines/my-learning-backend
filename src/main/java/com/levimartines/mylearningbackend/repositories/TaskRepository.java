package com.levimartines.mylearningbackend.repositories;

import com.levimartines.mylearningbackend.models.entities.Task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
}
