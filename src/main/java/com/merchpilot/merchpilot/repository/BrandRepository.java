package com.merchpilot.merchpilot.repository;

import com.merchpilot.merchpilot.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
