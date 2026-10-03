package com.asap.api.batch.repository;

import com.asap.api.batch.domain.BatchOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BatchOrderRepository extends JpaRepository<BatchOrder, UUID> {
}
