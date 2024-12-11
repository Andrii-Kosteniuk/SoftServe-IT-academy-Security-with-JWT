package com.softserve.itacademy.todolist.dto.user;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRequestDto(
        @Pattern(regexp = "[A-Z][a-z]+",
                message = "Must start with a capital letter followed by one or more lowercase letters")

        String firstName,

        @Pattern(regexp = "[A-Z][a-z]+",
                message = "Must start with a capital letter followed by one or more lowercase letters")
        String lastName,

        @Pattern(regexp = "[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}", message = "Must be a valid e-mail address")
        String email,

        @Pattern(regexp = "[A-Za-z\\d]{6,}",
                message = "Must be minimum 6 symbols long, using digits and latin letters")
        @Pattern(regexp = ".*\\d.*",
                message = "Must contain at least one digit")
        @Pattern(regexp = ".*[A-Z].*",
                message = "Must contain at least one uppercase letter")
        @Pattern(regexp = ".*[a-z].*",
                message = "Must contain at least one lowercase letter")
        String password,

        @NotBlank(message = "The role can not be empty")
        String role) {
}
