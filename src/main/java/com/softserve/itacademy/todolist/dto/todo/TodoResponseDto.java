package com.softserve.itacademy.todolist.dto.todo;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.softserve.itacademy.todolist.model.ToDo;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TodoResponseDto {
    Long id;
    String title;
    LocalDateTime createdAt;
    long owner_id;

    public TodoResponseDto(ToDo toDo) {
        this.id = toDo.getId();
        this.title = toDo.getTitle();
        this.createdAt = toDo.getCreatedAt();
        this.owner_id = toDo.getOwner().getId();
    }
}
