package com.livrapp.api.delivery.repository;

import com.livrapp.api.delivery.domain.InterVillesExpedition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InterVillesExpeditionRepository extends JpaRepository<InterVillesExpedition, UUID> {
}
