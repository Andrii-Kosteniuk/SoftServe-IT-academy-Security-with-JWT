package com.softserve.itacademy.todolist.dto.state;

import jakarta.validation.constraints.NotBlank;

public record StateRequestDto(
        @NotBlank(message = "The 'name' cannot be empty")
        String name) {
}
