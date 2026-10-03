package com.asap.api.finance.repository;

import com.asap.api.delivery.domain.Delivery;
import com.asap.api.finance.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findByDelivery(Delivery delivery);
}
