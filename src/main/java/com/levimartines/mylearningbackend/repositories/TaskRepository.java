package com.levimartines.mylearningbackend.repositories;

import com.levimartines.mylearningbackend.models.entities.Task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("FROM Task task WHERE task.user.id = :userId")
    List<Task> findAllByUserId(Long userId);
}
