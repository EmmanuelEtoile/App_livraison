package com.asap.api.finance.repository;

import com.asap.api.finance.domain.Avertissement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AvertissementRepository extends JpaRepository<Avertissement, UUID> {
}
