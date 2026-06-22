package com.livrapp.api.finance.repository;

import com.livrapp.api.finance.domain.Avertissement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AvertissementRepository extends JpaRepository<Avertissement, UUID> {
}
