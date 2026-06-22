package com.livrapp.api.delivery.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "receivers")
public class Receiver {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 20)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @Column(name = "has_app", nullable = false)
    private boolean hasApp = false;

    @Column(name = "otp_code", length = 10)
    private String otpCode;

    @Column(name = "otp_expires_at")
    private OffsetDateTime otpExpiresAt;

    @Column(name = "otp_verified", nullable = false)
    private boolean otpVerified = false;

    @Column(name = "otp_attempts", nullable = false)
    private int otpAttempts = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
