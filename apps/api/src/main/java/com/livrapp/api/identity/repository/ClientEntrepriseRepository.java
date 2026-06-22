package com.livrapp.api.identity.repository;

import com.livrapp.api.identity.domain.ClientEntreprise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClientEntrepriseRepository extends JpaRepository<ClientEntreprise, UUID> {

    Optional<ClientEntreprise> findByRccm(String rccm);
}
