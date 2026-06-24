package com.livrapp.api.identity;

import com.livrapp.api.identity.dto.KycDecisionRequest;
import com.livrapp.api.identity.dto.LivreurSummary;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/livreurs")
@PreAuthorize("hasAnyRole('ADMIN','SUPER_ADMIN')")
public class KycAdminController {

    private final KycService kycService;

    public KycAdminController(KycService kycService) {
        this.kycService = kycService;
    }

    @GetMapping("/pending")
    public List<LivreurSummary> pending() {
        return kycService.listPending();
    }

    @PostMapping("/{id}/kyc")
    public ResponseEntity<LivreurSummary> decide(@PathVariable UUID id,
                                                 @Valid @RequestBody KycDecisionRequest request) {
        return ResponseEntity.ok(kycService.decide(id, request.approved(), request.reason()));
    }
}
