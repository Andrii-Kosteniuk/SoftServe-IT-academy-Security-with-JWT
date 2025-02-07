package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.user.UserResponseDto;
import com.softserve.itacademy.todolist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    List<UserResponseDto> getAll() {
        return userService.getAll().stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }
}
