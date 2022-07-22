package com.levimartines.mylearningbackend.models.dtos;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO implements Serializable {
    private Long id;
    private String description;
    private boolean done = false;
    private LocalDate dueDate;
}
