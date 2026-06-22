package com.livrapp.api.batch.repository;

import com.livrapp.api.batch.domain.BatchDeliveryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BatchDeliveryItemRepository extends JpaRepository<BatchDeliveryItem, UUID> {
}
