package com.merchpilot.merchpilot.repository;

import com.merchpilot.merchpilot.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
