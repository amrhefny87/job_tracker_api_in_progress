package com.amrhefny.jobtracker.jobApplications;

import jakarta.validation.constraints.NotBlank;

public record JobApplicationDTO(
        @NotBlank
    String jobTitle,
    @NotBlank
    String companyName,
    String status,
    String companyLink,
    @NotBlank
    String jobLink,
    String notes,
    Long app_user_id
) {
}
