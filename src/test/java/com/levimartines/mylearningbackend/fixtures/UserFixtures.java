package com.levimartines.mylearningbackend.fixtures;

import com.levimartines.mylearningbackend.models.entities.User;

public class UserFixtures {

    public static final User DEFAULT_ADMIN = User.builder()
        .email("admin@admin.com")
        .password("admin")
        .admin(true)
        .build();

    public static final User DEFAULT_USER = User.builder()
        .email("user@user.com")
        .password("user")
        .admin(false)
        .build();
}
