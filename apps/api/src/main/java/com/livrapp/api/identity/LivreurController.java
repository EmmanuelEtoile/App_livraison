package com.livrapp.api.identity;

import com.livrapp.api.auth.SecurityUser;
import com.livrapp.api.identity.dto.LivreurSummary;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/livreurs")
public class LivreurController {

    private final KycService kycService;

    public LivreurController(KycService kycService) {
        this.kycService = kycService;
    }

    @PostMapping("/me/kyc/resubmit")
    @PreAuthorize("hasRole('LIVREUR')")
    public LivreurSummary resubmit(@AuthenticationPrincipal SecurityUser principal) {
        return kycService.resubmit(principal.getUser().getId());
    }
}
