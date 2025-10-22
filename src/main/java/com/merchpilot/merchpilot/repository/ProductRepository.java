package com.merchpilot.merchpilot.repository;

import com.merchpilot.merchpilot.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
