package com.softserve.itacademy.todolist.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskRequestDto(
        @NotBlank(message = "The 'name' cannot be empty")
        String name,

        @NotNull
        String priority) {
}
