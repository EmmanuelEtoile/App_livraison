package com.livrapp.api.delivery.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "partner_deposits")
public class PartnerDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partner_id", nullable = false)
    private PickupPartner partner;

    @CreationTimestamp
    @Column(name = "deposited_at", nullable = false, updatable = false)
    private OffsetDateTime depositedAt;

    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;            // dépôt + 72 h

    @Column(name = "retrieved_at")
    private OffsetDateTime retrievedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DepositStatus status = DepositStatus.DEPOSITED;

    @Column(name = "extension_days", nullable = false)
    private int extensionDays = 0;

    @Column(name = "extension_fees", nullable = false, precision = 12, scale = 2)
    private BigDecimal extensionFees = BigDecimal.ZERO;

    @Column(name = "expiry_photo_url", columnDefinition = "text")
    private String expiryPhotoUrl;
}
