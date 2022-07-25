package com.levimartines.mylearningbackend.security;

import com.levimartines.mylearningbackend.exceptions.SecurityContextException;
import com.levimartines.mylearningbackend.models.entities.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PrincipalService {

    public static CustomUserDetails authenticated() {
        try {
            return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

    public static User getUser() {
        CustomUserDetails authenticated = authenticated();
         if (authenticated == null || authenticated.getUser() == null) {
            throw new SecurityContextException("Authenticated user should not be null");
        }
        return authenticated.getUser();
    }

    public static Long getUserId() {
        return getUser().getId();
    }
}
