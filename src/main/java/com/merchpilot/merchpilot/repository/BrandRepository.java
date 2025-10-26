package com.merchpilot.merchpilot.repository;

import com.merchpilot.merchpilot.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {

    boolean existsByNameIgnoreCase(String name);
    Optional<Brand> findByNameIgnoreCase(String name);
}
