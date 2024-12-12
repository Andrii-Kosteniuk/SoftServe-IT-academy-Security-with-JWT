package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.config.auth.AuthenticationService;
import com.softserve.itacademy.todolist.config.authDto.AuthenticationResponse;
import com.softserve.itacademy.todolist.dto.user.UserRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDto userRequestDto) {
        try {
            AuthenticationResponse authenticationResponse = authenticationService.authenticate(userRequestDto);
            return ResponseEntity.ok().body(authenticationResponse);
        } catch (
                BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDto userRequestDto) {
        try {
            AuthenticationResponse authenticationResponse = authenticationService.register(userRequestDto);
            return ResponseEntity.ok().body(authenticationResponse);
        } catch (
                BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);

    }
}
