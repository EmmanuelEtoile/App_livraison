package com.livrapp.api.batch.domain;

import com.livrapp.api.identity.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "batch_orders")
public class BatchOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "structure_id", nullable = false)
    private User structure;

    @Enumerated(EnumType.STRING)
    @Column(name = "scheduled_day", nullable = false, length = 10)
    private ScheduledDay scheduledDay;

    @Column(name = "scheduled_hour", nullable = false)
    private LocalTime scheduledHour;

    @Column(name = "file_url", columnDefinition = "text")
    private String fileUrl;

    @Column(name = "total_orders", nullable = false)
    private int totalOrders = 0;

    @Column(name = "urgent_orders", nullable = false)
    private int urgentOrders = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BatchOrderStatus status = BatchOrderStatus.PENDING;

    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "processed_at")
    private OffsetDateTime processedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
