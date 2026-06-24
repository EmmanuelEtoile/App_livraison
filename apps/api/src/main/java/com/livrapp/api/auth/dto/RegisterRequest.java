package com.livrapp.api.auth.dto;

import com.livrapp.api.identity.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Email String email,
        @NotBlank String phone,
        @NotBlank @Size(min = 8, message = "Le mot de passe doit faire au moins 8 caractères") String password,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotNull Role role
) {
}
