package com.livrapp.api.batch.domain;

import com.livrapp.api.delivery.domain.Delivery;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "batch_delivery_items")
public class BatchDeliveryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "batch_order_id", nullable = false)
    private BatchOrder batchOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;            // nullable tant que non convertie

    @Column(name = "is_urgent", nullable = false)
    private boolean urgent = false;

    @Column(name = "receiver_name", nullable = false)
    private String receiverName;

    @Column(name = "receiver_phone", nullable = false, length = 20)
    private String receiverPhone;

    @Column(nullable = false, columnDefinition = "text")
    private String address;

    @Column(nullable = false, columnDefinition = "text")
    private String landmarks;

    @Column(name = "weight_kg", precision = 8, scale = 2)
    private BigDecimal weightKg;

    @Column(name = "payment_mode", length = 20)
    private String paymentMode;           // domaine ouvert -> String

    @Column(name = "pickup_order")
    private Integer pickupOrder;

    @Column(length = 20)
    private String status = "PENDING";    // domaine ouvert -> String
}
