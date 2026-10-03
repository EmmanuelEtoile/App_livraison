package com.asap.api.identity.dto;

import com.asap.api.identity.domain.KycStatus;

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
