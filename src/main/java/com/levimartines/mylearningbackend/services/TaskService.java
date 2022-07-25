package com.levimartines.mylearningbackend.services;

import com.levimartines.mylearningbackend.exceptions.NotFoundException;
import com.levimartines.mylearningbackend.models.entities.Task;
import com.levimartines.mylearningbackend.models.vos.TaskVO;
import com.levimartines.mylearningbackend.repositories.TaskRepository;
import com.levimartines.mylearningbackend.security.PrincipalService;

import java.util.List;
import java.util.Optional;

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
        return repository.findAllByUserId(PrincipalService.getUserId());
    }

    public Task create(TaskVO body) {
        Task task = mapper.map(body, Task.class);
        setUser(task);
        return repository.save(task);
    }

    private void setUser(Task task) {
        task.setUser(PrincipalService.getUser());
    }

    public void markAsDone(Long id) {
        Task task = findById(id);
        task.setDone(true);
        repository.save(task);
    }

    private Task findById(Long id) {
        Optional<Task> task = repository.findById(id);
        if (task.isEmpty()) {
            log.error("User with id [{}] not found", id);
            throw new NotFoundException("Task not found");
        }
        validateOwner(task.get());
        return task.get();
    }

    private void validateOwner(Task task) {
        Long loggedUserId = PrincipalService.getUserId();
        if (!loggedUserId.equals(task.getUser().getId())) {
            log.error("User [{}] is not the owner of the task [{}]", loggedUserId, task.getId());
            throw new NotFoundException("Task not found");
        }
    }

    public void delete(Long id) {
        Task task = findById(id);
        repository.delete(task);
    }
}
