package com.merchpilot.merchpilot.controller;


import com.merchpilot.merchpilot.common.web.ResponseTransaction;
import com.merchpilot.merchpilot.dto.product.ProductRequest;
import com.merchpilot.merchpilot.dto.product.ProductResponse;
import com.merchpilot.merchpilot.dto.product.ProductUpdateRequest;
import com.merchpilot.merchpilot.entity.Product;
import com.merchpilot.merchpilot.repository.ProductRepository;
import com.merchpilot.merchpilot.services.InventoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final InventoryService inventoryService;
    private final ProductRepository productRepository;

    public ProductController(InventoryService inventoryService, ProductRepository productRepository) {
        this.inventoryService = inventoryService;
        this.productRepository = productRepository;
    }

    @PostMapping
    public ResponseTransaction create(@Valid @RequestBody ProductRequest req) {
        return ResponseTransaction.buildSuccessResponse(inventoryService.create(req));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> responses = products.stream()
                .map(ProductResponse::of)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseTransaction get(@PathVariable Long id) {
        return ResponseTransaction.buildSuccessResponse(inventoryService.get(id));
    }

    @GetMapping
    public ResponseTransaction search(@RequestParam(required = false) String q,
                                      @RequestParam(required = false) Long brandId,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size) {
        Page<ProductResponse> p = inventoryService.search(q, brandId, page, size);
        return ResponseTransaction.buildSuccessResponse(p);
    }

    @PutMapping("/{id}")
    public ResponseTransaction update(@PathVariable Long id,
                                      @Valid @RequestBody ProductUpdateRequest req) {
        return ResponseTransaction.buildSuccessResponse(inventoryService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseTransaction delete(@PathVariable Long id) {
        inventoryService.delete(id);
        return ResponseTransaction.buildSuccessResponse(null, "Deleted");
    }

    @PostMapping("/{id}/stock")
    public ResponseTransaction adjustStock(@PathVariable Long id, @RequestParam int delta) {
        return ResponseTransaction.buildSuccessResponse(inventoryService.adjustStock(id, delta));
    }

    @GetMapping("/stats")
    public ResponseTransaction stats() {
        return ResponseTransaction.buildSuccessResponse(inventoryService.stats());
    }
}
