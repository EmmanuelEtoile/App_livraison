package com.asap.api.delivery.repository;

import com.asap.api.delivery.domain.PickupPartner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PickupPartnerRepository extends JpaRepository<PickupPartner, UUID> {
}
