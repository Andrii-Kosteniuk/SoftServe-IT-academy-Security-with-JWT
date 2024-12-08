package com.softserve.itacademy.todolist.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Entity
@Table(name = "todos")
public class ToDo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The 'title' cannot be empty")
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "todo", cascade = CascadeType.REMOVE)
    private List<Task> tasks;

    @ManyToMany
    @JoinTable(name = "todo_collaborator",
            joinColumns = @JoinColumn(name = "todo_id"),
            inverseJoinColumns = @JoinColumn(name = "collaborator_id"))
    private List<User> collaborators;

}
