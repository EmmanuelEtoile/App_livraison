package com.livrapp.api.finance.domain;

import com.livrapp.api.delivery.domain.Delivery;
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
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 5)
    private String currency = "XAF";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "transaction_ref")
    private String transactionRef;

    @Column(name = "cash_photo_url", columnDefinition = "text")
    private String cashPhotoUrl;

    @Column(name = "cash_amount_declared", precision = 12, scale = 2)
    private BigDecimal cashAmountDeclared;

    @Column(name = "cash_client_otp", length = 10)
    private String cashClientOtp;

    @Column(name = "cash_otp_verified", nullable = false)
    private boolean cashOtpVerified = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
