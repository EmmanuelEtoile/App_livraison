package com.livrapp.api.delivery.repository;

import com.livrapp.api.delivery.domain.Receiver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReceiverRepository extends JpaRepository<Receiver, UUID> {
}
