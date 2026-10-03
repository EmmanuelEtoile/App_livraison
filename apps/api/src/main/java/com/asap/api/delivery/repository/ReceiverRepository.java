package com.asap.api.delivery.repository;

import com.asap.api.delivery.domain.Receiver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReceiverRepository extends JpaRepository<Receiver, UUID> {
}
