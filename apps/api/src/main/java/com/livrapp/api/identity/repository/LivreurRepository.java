package com.livrapp.api.identity.repository;

import com.livrapp.api.identity.domain.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LivreurRepository extends JpaRepository<Livreur, UUID> {
}
