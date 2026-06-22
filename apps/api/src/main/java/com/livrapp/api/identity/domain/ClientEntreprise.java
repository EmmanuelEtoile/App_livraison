package com.livrapp.api.identity.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "clients_entreprise")
public class ClientEntreprise {

    @Id
    private UUID userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(nullable = false, unique = true, length = 100)
    private String rccm;

    @Column(name = "billing_email", nullable = false)
    private String billingEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Tier tier = Tier.STANDARD;

    @Column(name = "monthly_volume", nullable = false)
    private int monthlyVolume = 0;

    @Column(name = "contract_url", columnDefinition = "text")
    private String contractUrl;

    @Column(name = "contract_signed_at")
    private OffsetDateTime contractSignedAt;
}
