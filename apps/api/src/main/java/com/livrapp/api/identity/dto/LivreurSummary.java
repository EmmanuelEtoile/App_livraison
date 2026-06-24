package com.livrapp.api.identity.dto;

import com.livrapp.api.identity.domain.KycStatus;

import java.util.UUID;

public record LivreurSummary(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        KycStatus kycStatus
) {
}
