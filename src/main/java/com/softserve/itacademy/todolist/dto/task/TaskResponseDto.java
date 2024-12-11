package com.softserve.itacademy.todolist.dto.task;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.softserve.itacademy.todolist.model.Task;
import lombok.Value;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TaskResponseDto {
    long id;
    String name;
    String priority;
    long todo_id;
    String state;

    public TaskResponseDto(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.priority = task.getPriority().name();
        this.todo_id = task.getTodo().getId();
        this.state = task.getState().getName();
    }
}
