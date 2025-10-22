package com.merchpilot.merchpilot.repository;


import com.merchpilot.merchpilot.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Order, Long> {
}