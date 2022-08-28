package com.levimartines.mylearningbackend.controllers;

import com.levimartines.mylearningbackend.models.dtos.UserDTO;
import com.levimartines.mylearningbackend.models.entities.User;
import com.levimartines.mylearningbackend.models.vos.UserVO;
import com.levimartines.mylearningbackend.services.UserService;

import java.net.URI;
import java.util.List;

import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;
    private final ModelMapper mapper;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid UserVO body, HttpServletRequest request) {
        User user = service.create(body);
        URI uri = ServletUriComponentsBuilder.fromRequest(request)
            .path("/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        User user = service.findById(id);
        return ResponseEntity.ok(mapper.map(user, UserDTO.class));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<UserDTO>> findAll() {
        List<User> users = service.findAll();
        List<UserDTO> response = users.stream().map(user -> mapper.map(user, UserDTO.class)).toList();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/confirm-registration")
    public ResponseEntity<Void> confirmRegistration(@RequestParam("code") String code) {
        service.confirmRegistration(code);
        return ResponseEntity.noContent().build();
    }
}
