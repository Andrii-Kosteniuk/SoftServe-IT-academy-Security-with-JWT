package com.softserve.itacademy.todolist.dto.state;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.softserve.itacademy.todolist.model.State;
import com.softserve.itacademy.todolist.model.Task;
import lombok.Value;

import java.util.List;

@Value
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StateResponseDto {

    long id;
    String name;
    List<Task> tasks;

    public StateResponseDto(State state) {
        this.id = state.getId();
        this.name = state.getName();
        this.tasks = state.getTasks();
    }
}
