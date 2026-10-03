package com.asap.api.finance.repository;

import com.asap.api.finance.domain.Penalite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PenaliteRepository extends JpaRepository<Penalite, UUID> {
}
