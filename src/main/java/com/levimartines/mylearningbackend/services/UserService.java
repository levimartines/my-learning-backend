package com.levimartines.mylearningbackend.services;

import com.levimartines.mylearningbackend.dtos.UserDto;
import com.levimartines.mylearningbackend.entities.User;
import com.levimartines.mylearningbackend.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public User create(User user) {
        user.setId(null);
        return userRepository.save(user);
    }

    public User fromDto(UserDto dto) {
        return User.builder()
            .email(dto.getEmail())
            .password(encoder.encode(dto.getPassword()))
            .admin(false)
            .build();
    }
}
