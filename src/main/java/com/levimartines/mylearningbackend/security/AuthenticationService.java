package com.levimartines.mylearningbackend.security;

import com.levimartines.mylearningbackend.models.entities.User;
import com.levimartines.mylearningbackend.repositories.UserRepository;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.jboss.aerogear.security.otp.Totp;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.levimartines.mylearningbackend.utils.Number.isValidLong;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    public void checkMFA(String email, String verificationCode) {
        Optional<User> optional = userRepository.findByEmailIgnoreCase(email);
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException(email);
        }
        User user = optional.get();
        if (user.isUsingMfa()) {
            Totp totp = new Totp(user.getSecret());
            if (!isValidLong(verificationCode) || !totp.verify(verificationCode)) {
                throw new BadCredentialsException("Invalid verification code");
            }
        }
    }

}
