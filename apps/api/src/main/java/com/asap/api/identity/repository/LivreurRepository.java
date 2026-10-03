package com.asap.api.identity.repository;

import com.asap.api.identity.domain.KycStatus;
import com.asap.api.identity.domain.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LivreurRepository extends JpaRepository<Livreur, UUID> {

    List<Livreur> findByKycStatus(KycStatus kycStatus);
}
