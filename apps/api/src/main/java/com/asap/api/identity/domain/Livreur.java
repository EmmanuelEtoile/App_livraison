package com.asap.api.identity.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "kyc_status", nullable = false, length = 20)
    private KycStatus kycStatus = KycStatus.PENDING;

    // --- Capacité de transport déclarée au KYC (modèle hybride) ---
    @Column(name = "has_own_vehicle", nullable = false)
    private boolean hasOwnVehicle = false;

    // Valeurs issues de l'enum TransportMode (validées côté service/DTO).
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "transport_modes", columnDefinition = "text[]")
    private String[] transportModes;

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
