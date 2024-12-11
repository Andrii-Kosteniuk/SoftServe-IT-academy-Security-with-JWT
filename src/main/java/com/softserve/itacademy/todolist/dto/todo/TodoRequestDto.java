package com.softserve.itacademy.todolist.dto.todo;

import jakarta.validation.constraints.NotBlank;

public record TodoRequestDto(
        @NotBlank(message = "The 'title' cannot be empty")
        String title) {
}
