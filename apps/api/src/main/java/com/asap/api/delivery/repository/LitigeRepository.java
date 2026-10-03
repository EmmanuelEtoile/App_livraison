package com.asap.api.delivery.repository;

import com.asap.api.delivery.domain.Litige;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LitigeRepository extends JpaRepository<Litige, UUID> {
}
