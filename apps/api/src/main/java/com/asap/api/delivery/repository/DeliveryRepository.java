package com.asap.api.delivery.repository;

import com.asap.api.delivery.domain.Delivery;
import com.asap.api.delivery.domain.DeliveryStatus;
import com.asap.api.identity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

    List<Delivery> findByClient(User client);

    List<Delivery> findByStatus(DeliveryStatus status);

    List<Delivery> findByLivreurAndStatus(User livreur, DeliveryStatus status);
}
