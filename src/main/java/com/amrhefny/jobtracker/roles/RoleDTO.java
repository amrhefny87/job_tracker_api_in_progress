package com.amrhefny.jobtracker.roles;

import jakarta.validation.constraints.NotBlank;

public record RoleDTO(
        @NotBlank
        String role
) {
}
