package com.softserve.itacademy.todolist.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Data
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The 'name' cannot be empty")
    @Column(name = "name", nullable = false, unique = true)
    private String name;


    @Override
    public String getAuthority() {
        return "ROLE_" + name;
    }
}
