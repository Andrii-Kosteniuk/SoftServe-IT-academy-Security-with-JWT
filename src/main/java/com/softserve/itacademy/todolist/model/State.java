package com.softserve.itacademy.todolist.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.Hibernate;

import java.util.List;


@Data
@Entity
@Table(name = "states")
public class State {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The 'name' cannot be empty")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "state")
    private List<Task> tasks;

}
