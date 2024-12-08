package com.softserve.itacademy.todolist.config.authDto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class AuthenticationResponse {
    private String username;
    private String token;
}
