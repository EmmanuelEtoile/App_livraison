package com.asap.api.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String identifier,   // email OU téléphone
        @NotBlank String password
) {
}
