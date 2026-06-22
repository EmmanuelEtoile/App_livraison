package com.livrapp.api.identity.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "livreurs")
public class Livreur {

    // La clé primaire EST la clé étrangère vers users (clé primaire partagée).
    @Id
    private UUID userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "mode_transport", length = 50)
    private String modeTransport;

    @Enumerated(EnumType.STRING)
    @Column(name = "kyc_status", nullable = false, length = 20)
    private KycStatus kycStatus = KycStatus.PENDING;

    @Column(name = "current_location", columnDefinition = "geography(Point,4326)")
    private Point currentLocation;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal score = new BigDecimal("100.00");

    @Column(name = "completed_missions", nullable = false)
    private int completedMissions = 0;

    @Column(name = "is_available", nullable = false)
    private boolean available = false;

    @Column(name = "wallet_balance", nullable = false, precision = 12, scale = 2)
    private BigDecimal walletBalance = BigDecimal.ZERO;

    @Column(name = "contract_signed_at")
    private OffsetDateTime contractSignedAt;
}
