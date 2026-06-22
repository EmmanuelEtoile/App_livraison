package com.livrapp.api.delivery.repository;

import com.livrapp.api.delivery.domain.Delivery;
import com.livrapp.api.delivery.domain.DeliveryStatus;
import com.livrapp.api.identity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

    List<Delivery> findByClient(User client);

    List<Delivery> findByStatus(DeliveryStatus status);

    List<Delivery> findByLivreurAndStatus(User livreur, DeliveryStatus status);
}
