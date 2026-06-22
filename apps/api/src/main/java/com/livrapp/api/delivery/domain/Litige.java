package com.livrapp.api.delivery.domain;

import com.livrapp.api.identity.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "litiges")
public class Litige {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reported_by", nullable = false)
    private User reportedBy;

    @Column(length = 30)
    private String type;                   // domaine ouvert -> String

    @Column(nullable = false, length = 20)
    private String status = "OPEN";        // domaine ouvert -> String

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "evidence_photos", columnDefinition = "text[]")
    private String[] evidencePhotos;

    @Column(columnDefinition = "text")
    private String resolution;

    @Column(name = "deduction_amount", precision = 12, scale = 2)
    private BigDecimal deductionAmount;

    @Column(name = "sla_hours")
    private Integer slaHours;

    @Column(name = "closed_at")
    private OffsetDateTime closedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
