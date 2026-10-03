package com.asap.api.delivery.repository;

import com.asap.api.delivery.domain.PartnerDeposit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PartnerDepositRepository extends JpaRepository<PartnerDeposit, UUID> {
}
