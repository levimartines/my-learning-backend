package com.levimartines.mylearningbackend.services;

import com.levimartines.mylearningbackend.models.entities.Task;
import com.levimartines.mylearningbackend.repositories.TaskRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    public List<Task> findAll() {
        return repository.findAll();
    }
}
