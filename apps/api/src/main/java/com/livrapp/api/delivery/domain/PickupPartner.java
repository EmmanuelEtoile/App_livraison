package com.livrapp.api.delivery.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "pickup_partners")
public class PickupPartner {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(nullable = false, columnDefinition = "text")
    private String address;

    @Column(columnDefinition = "geography(Point,4326)", nullable = false)
    private Point location;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "opening_hours", columnDefinition = "jsonb")
    private String openingHours;

    @Column(name = "commission_per_colis", nullable = false, precision = 10, scale = 2)
    private BigDecimal commissionPerColis = new BigDecimal("150.00");

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "total_packages", nullable = false)
    private int totalPackages = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
