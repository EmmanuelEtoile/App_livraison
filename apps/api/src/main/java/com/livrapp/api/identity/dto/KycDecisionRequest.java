package com.livrapp.api.identity.dto;

import jakarta.validation.constraints.NotNull;

public record KycDecisionRequest(
        @NotNull Boolean approved,
        String reason   // requis uniquement en cas de rejet (vérifié dans le service)
) {
}
