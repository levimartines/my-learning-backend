package com.levimartines.mylearningbackend.services;

import com.levimartines.mylearningbackend.exceptions.NotFoundException;
import com.levimartines.mylearningbackend.models.vos.UserVO;
import com.levimartines.mylearningbackend.models.entities.User;
import com.levimartines.mylearningbackend.repositories.UserRepository;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;

    public User create(UserVO dto) {
        User user = fromDto(dto);
        user.setId(null);
        return repository.save(user);
    }

    public User findById(Long id) {
        Optional<User> user = repository.findById(id);
        if (user.isEmpty()) {
            log.error("User with id [{}] not found", id);
            throw new NotFoundException("User not found");
        }
        return user.get();
    }

    public User fromDto(UserVO dto) {
        return User.builder()
            .email(dto.getEmail())
            .password(encoder.encode(dto.getPassword()))
            .admin(false)
            .build();
    }
}
