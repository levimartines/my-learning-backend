package com.levimartines.mylearningbackend.controllers;

import com.levimartines.mylearningbackend.models.dtos.TaskDTO;
import com.levimartines.mylearningbackend.models.entities.Task;
import com.levimartines.mylearningbackend.models.vos.TaskVO;
import com.levimartines.mylearningbackend.services.TaskService;

import java.util.List;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;
    private final ModelMapper mapper;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid TaskVO body) {
        service.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> findAll() {
        List<Task> tasks = service.findAll();
        List<TaskDTO> response = tasks.stream().map(t -> mapper.map(t, TaskDTO.class)).toList();
        return ResponseEntity.ok(response);
    }
}
