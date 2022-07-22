package com.levimartines.mylearningbackend.services;

import com.levimartines.mylearningbackend.models.entities.Task;
import com.levimartines.mylearningbackend.models.vos.TaskVO;
import com.levimartines.mylearningbackend.repositories.TaskRepository;
import com.levimartines.mylearningbackend.security.PrincipalService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;
    private final ModelMapper mapper;

    public List<Task> findAll() {
        return repository.findByUserId(PrincipalService.getId());
    }

    public Task create(TaskVO body) {
        Task task = mapper.map(body, Task.class);
        setUser(task);
        return repository.save(task);
    }

    private void setUser(Task task) {
        task.setUser(PrincipalService.getUser());
    }
}
