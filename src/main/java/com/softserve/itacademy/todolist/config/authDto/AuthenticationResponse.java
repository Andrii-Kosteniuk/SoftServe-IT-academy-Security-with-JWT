package com.softserve.itacademy.todolist.config.authDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    private String username;
    private String message;
    private String accessToken;
    private String refreshToken;
}
