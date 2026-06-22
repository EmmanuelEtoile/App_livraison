package com.livrapp.api.finance.domain;

import com.livrapp.api.delivery.domain.Delivery;
import com.livrapp.api.identity.domain.User;
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
@Table(name = "penalites")
public class Penalite {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "target_user_id", nullable = false)
    private User targetUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private PenaliteType type;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "livreur_share", precision = 12, scale = 2)
    private BigDecimal livreurShare;

    @Column(name = "platform_share", precision = 12, scale = 2)
    private BigDecimal platformShare;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PenaliteStatus status = PenaliteStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collected_on_delivery_id")
    private Delivery collectedOnDelivery;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
