package com.livrapp.api.delivery.domain;

import com.livrapp.api.common.domain.Auditable;
import com.livrapp.api.identity.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "deliveries")
public class Delivery extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // --- Relations ---
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livreur_id")
    private User livreur;                 // nul tant que non attribuée

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Receiver receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_pickup_id")
    private PickupPartner partnerPickup;

    @Column(name = "batch_item_id")
    private UUID batchItemId;             // relation ajoutée au Lot 3 (BatchDeliveryItem)

    // --- Caractéristiques ---
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_type", nullable = false, length = 20)
    private DeliveryType deliveryType = DeliveryType.STANDARD;

    @Column(name = "pickup_location", columnDefinition = "geography(Point,4326)", nullable = false)
    private Point pickupLocation;

    @Column(name = "pickup_address", nullable = false, columnDefinition = "text")
    private String pickupAddress;

    @Column(name = "pickup_landmarks", nullable = false, columnDefinition = "text")
    private String pickupLandmarks;

    @Column(name = "delivery_location", columnDefinition = "geography(Point,4326)", nullable = false)
    private Point deliveryLocation;

    @Column(name = "delivery_address", nullable = false, columnDefinition = "text")
    private String deliveryAddress;

    @Column(name = "scheduled_date")
    private LocalDate scheduledDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "scheduled_slot", length = 20)
    private ScheduledSlot scheduledSlot;

    // --- Statut & cycle de vie ---
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private DeliveryStatus status = DeliveryStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "urgency_level", nullable = false, length = 20)
    private UrgencyLevel urgencyLevel = UrgencyLevel.STANDARD;

    // --- Tarification ---
    @Column(name = "weight_kg", precision = 8, scale = 2)
    private BigDecimal weightKg;

    @Column(name = "zone_supplement", nullable = false, precision = 8, scale = 2)
    private BigDecimal zoneSupplement = BigDecimal.ZERO;

    @Column(name = "estimated_price", precision = 12, scale = 2)
    private BigDecimal estimatedPrice;

    @Column(name = "final_price", precision = 12, scale = 2)
    private BigDecimal finalPrice;

    @Column(name = "platform_commission", precision = 12, scale = 2)
    private BigDecimal platformCommission;     // 20 %

    @Column(name = "livreur_share", precision = 12, scale = 2)
    private BigDecimal livreurShare;           // 80 %

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    // --- Divers ---
    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    private String[] photos;

    @Column(name = "otp_used", nullable = false)
    private boolean otpUsed = false;

    @Column(name = "distance_km", precision = 8, scale = 2)
    private BigDecimal distanceKm;

    @Column(name = "eta_minutes")
    private Integer etaMinutes;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;
}
