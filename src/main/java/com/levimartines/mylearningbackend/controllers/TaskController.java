package com.levimartines.mylearningbackend.controllers;

import com.levimartines.mylearningbackend.models.dtos.TaskDTO;
import com.levimartines.mylearningbackend.models.entities.Task;
import com.levimartines.mylearningbackend.models.vos.TaskVO;
import com.levimartines.mylearningbackend.security.CustomUserDetails;
import com.levimartines.mylearningbackend.services.TaskService;

import java.net.URI;
import java.util.List;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;
    private final ModelMapper mapper;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid TaskVO body, HttpServletRequest request) {
        Task task = service.create(body);
        URI uri = ServletUriComponentsBuilder.fromRequest(request)
            .path("/{id}").buildAndExpand(task.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> findAll(@AuthenticationPrincipal CustomUserDetails customUser) {
        List<Task> tasks = service.findAll();
        List<TaskDTO> response = tasks.stream().map(t -> mapper.map(t, TaskDTO.class)).toList();
        return ResponseEntity.ok(response);
    }
}
