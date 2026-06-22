package com.livrapp.api.delivery.repository;

import com.livrapp.api.delivery.domain.PickupPartner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PickupPartnerRepository extends JpaRepository<PickupPartner, UUID> {
}
