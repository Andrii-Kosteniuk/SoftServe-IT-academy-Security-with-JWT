package com.softserve.itacademy.todolist.dto.user;


import com.softserve.itacademy.todolist.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
        @Pattern(regexp = "[A-Z][a-z]+",
                message = "Must start with a capital letter followed by one or more lowercase letters")
        private String firstName;

        @Pattern(regexp = "[A-Z][a-z]+",
                message = "Must start with a capital letter followed by one or more lowercase letters")
        private String lastName;

        @Pattern(regexp = "[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}", message = "Must be a valid e-mail address")
        private String email;

//        @Pattern(regexp = "[A-Za-z\\d]{6,}",
//                message = "Must be minimum 6 symbols long, using digits and latin letters")
//        @Pattern(regexp = ".*\\d.*",
//                message = "Must contain at least one digit")
//        @Pattern(regexp = ".*[A-Z].*",
//                message = "Must contain at least one uppercase letter")
//        @Pattern(regexp = ".*[a-z].*",
//                message = "Must contain at least one lowercase letter")
        private String password;

        @NotBlank(message = "The role can not be empty")
        private  Role role;

}
