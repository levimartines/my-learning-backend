package com.levimartines.mylearningbackend.services;

import com.levimartines.mylearningbackend.exceptions.NotFoundException;
import com.levimartines.mylearningbackend.models.vos.UserVO;
import com.levimartines.mylearningbackend.models.entities.User;
import com.levimartines.mylearningbackend.repositories.UserRepository;
import com.levimartines.mylearningbackend.security.PrincipalService;

import java.util.List;
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
        log.info("Creating new User [{}]", dto.getEmail());
        return repository.save(user);
    }

    public User findById(Long id) {
        checkPermissionToRetrieve(id);
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

    public void setUseMFA(Long id, boolean useMFA) {
        User user = findById(id);
        user.setUsingMfa(useMFA);

        log.info("{} MFA for User [{}]", useMFA ? "Enabling" : "Disabling", user.getEmail());
        repository.save(user);
    }

    private void checkPermissionToRetrieve(Long id) {
        User loggedUser = PrincipalService.getUser();
        if (loggedUser.isNotAdmin() && !loggedUser.getId().equals(id)) {
            throw new NotFoundException("User not found");
        }
    }

    public List<User> findAll() {
        return repository.findAll();
    }
}
