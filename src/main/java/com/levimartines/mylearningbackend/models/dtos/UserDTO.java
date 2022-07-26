package com.levimartines.mylearningbackend.models.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private boolean usingMfa;
    private boolean registrationConfirmed;
}
