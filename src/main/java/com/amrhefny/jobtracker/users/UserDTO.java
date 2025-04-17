package com.amrhefny.jobtracker.users;

import jakarta.validation.constraints.NotBlank;

public record UserDTO(
        @NotBlank
        String userName,
        String firstName,
        String lastName,
        String jobTitle,
        @NotBlank
        String email,
        @NotBlank
        String password,
        String role
) {
}
