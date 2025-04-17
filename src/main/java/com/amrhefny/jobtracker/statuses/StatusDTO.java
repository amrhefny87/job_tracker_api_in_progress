package com.amrhefny.jobtracker.statuses;

import jakarta.validation.constraints.NotBlank;

public record StatusDTO(
        @NotBlank
        String status
) {
}
