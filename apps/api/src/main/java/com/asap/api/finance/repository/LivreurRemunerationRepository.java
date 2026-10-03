package com.asap.api.finance.repository;

import com.asap.api.finance.domain.LivreurRemuneration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LivreurRemunerationRepository extends JpaRepository<LivreurRemuneration, UUID> {
}
