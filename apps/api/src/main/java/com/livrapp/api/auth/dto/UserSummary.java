package com.livrapp.api.auth.dto;

import java.util.UUID;

public record UserSummary(
        UUID id,
        String email,
        String role,
        String firstName,
        String lastName
) {
}
