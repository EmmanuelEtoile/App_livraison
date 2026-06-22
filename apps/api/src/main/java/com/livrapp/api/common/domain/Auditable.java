package com.livrapp.api.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

/**
 * Classe de base mutualisée : horodatage automatique de création / mise à jour.
 * Réutilisable par toutes les entités qui ont created_at / updated_at.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class Auditable {

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
