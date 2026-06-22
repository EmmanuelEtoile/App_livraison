package com.livrapp.api.delivery.domain;

import com.livrapp.api.identity.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "inter_villes_expeditions")
public class InterVillesExpedition {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;

    @Column(name = "agency_name")
    private String agencyName;

    @Column(name = "agency_city", length = 100)
    private String agencyCity;

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @Column(name = "receipt_photo_url", columnDefinition = "text")
    private String receiptPhotoUrl;

    @Column(name = "package_photo_url", columnDefinition = "text")
    private String packagePhotoUrl;

    @Column(name = "estimated_arrival")
    private OffsetDateTime estimatedArrival;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_livreur_id")
    private User destinationLivreur;

    @Column(length = 20)
    private String status;                 // domaine ouvert -> String

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
