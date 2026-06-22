package com.livrapp.api.finance.repository;

import com.livrapp.api.finance.domain.LivreurRemuneration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LivreurRemunerationRepository extends JpaRepository<LivreurRemuneration, UUID> {
}
