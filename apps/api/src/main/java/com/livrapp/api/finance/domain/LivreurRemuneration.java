package com.livrapp.api.finance.domain;

import com.livrapp.api.identity.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "livreur_remunerations")
public class LivreurRemuneration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "livreur_id", nullable = false)
    private User livreur;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "total_missions", nullable = false)
    private int totalMissions = 0;

    @Column(name = "gross_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal grossAmount = BigDecimal.ZERO;   // 80 % cumulés + prime

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal deductions = BigDecimal.ZERO;

    @Column(name = "net_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal netAmount = BigDecimal.ZERO;

    @Column(name = "payment_method", length = 20)
    private String paymentMethod;          // domaine ouvert -> String

    @Column(nullable = false, length = 20)
    private String status = "PENDING";     // domaine ouvert -> String

    @Column(name = "transaction_ref")
    private String transactionRef;

    @Column(name = "processed_at")
    private OffsetDateTime processedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
