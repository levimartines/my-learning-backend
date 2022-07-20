package com.levimartines.mylearningbackend.models.dtos;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO implements Serializable {
    private Long id;
    private String description;
    private boolean done = false;
    private LocalDate dueDate;
}
