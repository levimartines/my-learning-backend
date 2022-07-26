package com.levimartines.mylearningbackend.services;

import com.levimartines.mylearningbackend.exceptions.NotFoundException;
import com.levimartines.mylearningbackend.models.entities.Task;
import com.levimartines.mylearningbackend.models.entities.User;
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
        return repository.findAllNotDoneByUserId(PrincipalService.getUserId());
    }

    public Task create(TaskVO body) {
        Task task = mapper.map(body, Task.class);
        setUser(task);
        log.info("Creating new task [{}] for user [{}]", task.getDescription(), task.getUser().getEmail());
        return repository.save(task);
    }

    public void markAsDone(Long id) {
        Task task = findById(id);
        task.setDone(true);
        repository.save(task);
    }

    public void delete(Long id) {
        Task task = findById(id);
        log.info("Deleting task [{}]", id);
        repository.delete(task);
    }

    private Task findById(Long id) {
        Optional<Task> task = repository.findById(id);
        if (task.isEmpty()) {
            log.error("Task with id [{}] not found", id);
            throw new NotFoundException("Task not found");
        }
        validateOwner(task.get());
        return task.get();
    }

    private void validateOwner(Task task) {
        User loggedUser = PrincipalService.getUser();
        if (!loggedUser.getId().equals(task.getUser().getId())) {
            log.error("User [{}] is not the owner of the task [{}]", loggedUser.getId(), task.getId());
            throw new NotFoundException("Task not found");
        }
    }

    private void setUser(Task task) {
        task.setUser(PrincipalService.getUser());
    }

}
