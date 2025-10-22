package com.merchpilot.merchpilot.services;

import com.merchpilot.merchpilot.repository.ProductRepository;

public class InventoryService {
    private final ProductRepository productRepository;

    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
