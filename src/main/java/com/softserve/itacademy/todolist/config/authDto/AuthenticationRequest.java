package com.softserve.itacademy.todolist.config.authDto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class AuthenticationRequest {
    private String username;
    private String password;
}

