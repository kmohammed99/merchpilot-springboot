package com.merchpilot.merchpilot.services;

import com.merchpilot.merchpilot.dto.product.*;
import com.merchpilot.merchpilot.entity.*;
import com.merchpilot.merchpilot.exception.BusinessException;
import com.merchpilot.merchpilot.exception.NotFoundException;
import com.merchpilot.merchpilot.repository.BrandRepository;
import com.merchpilot.merchpilot.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

    public InventoryService(ProductRepository productRepository, BrandRepository brandRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
    }

    /* ======================= Create ======================= */
    @Transactional
    public ProductResponse create(ProductRequest req) {
        Brand brand = brandRepository.findById(req.brandId())
                .orElseThrow(() -> new NotFoundException("Brand not found"));

        Product p = new Product();
        p.setBrand(brand);
        p.setName(req.name());
        p.setCategory(req.category());
        p.setSku(req.sku());
        p.setColors(new ArrayList<>(req.colors()));
        p.setSizes(new ArrayList<>(req.sizes()));
        p.setCostPrice(req.costPrice());
        p.setSellingPrice(req.sellingPrice());
        p.setMinimumStock(req.minimumStock());
        p.setTotalQuantity(req.totalQuantity());
        p.setTotalSell(0);
        p.setCreatedAt(OffsetDateTime.now());
        p.setUpdatedAt(OffsetDateTime.now());

        p = productRepository.save(p);
        return ProductResponse.of(p);
    }

    /* ======================= Read ======================= */
    public ProductResponse get(Long id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        return toResponse(p);
    }

    public Page<ProductResponse> search(String q, Long brandId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        return productRepository.search(q, brandId, pageable).map(this::toResponse);
    }

    /* ======================= Update ======================= */
    @Transactional
    public ProductResponse update(Long id, ProductUpdateRequest req) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (req.brandId() != null) {
            Brand brand = brandRepository.findById(req.brandId())
                    .orElseThrow(() -> new NotFoundException("Brand not found: " + req.brandId()));
            p.setBrand(brand);
        }

        if (req.sku() != null && !req.sku().equals(p.getSku())) {
            productRepository.findBySku(req.sku()).ifPresent(other -> {
                if (!other.getId().equals(id)) {
                    throw new BusinessException("SKU already exists: " + req.sku());
                }
            });
            p.setSku(req.sku());
        }

        if (req.name() != null) p.setName(req.name());
        if (req.category() != null) p.setCategory(req.category());

//        if (req.colors() != null) {
//            if (p.getColors() == null) p.setColors(new ArrayList<>());
//            p.getColors().clear();
//            p.getColors().addAll(req.colors());
//        }
//        if (req.sizes() != null) {
//            if (p.getSizes() == null) p.setSizes(new ArrayList<>());
//            p.getSizes().clear();
//            p.getSizes().addAll(req.sizes());
//        }

        if (req.costPrice() != null) p.setCostPrice(req.costPrice());
        if (req.sellingPrice() != null) p.setSellingPrice(req.sellingPrice());
        if (req.minimumStock() != null) p.setMinimumStock(req.minimumStock());
        if (req.totalQuantity() != null) p.setTotalQuantity(req.totalQuantity());

        p.setUpdatedAt(OffsetDateTime.now());

        return ProductResponse.of(productRepository.save(p));
    }


    /* ======================= Delete ======================= */
    @Transactional
    public void delete(Long id) {
        if (!productRepository.existsById(id))
            throw new NotFoundException("Product not found");
        productRepository.deleteById(id);
    }

    /* ======================= Stock Ops ======================= */

    /**
     * استلام/خصم مخزون (قد يكون بالسالب للخصم)
     */
    @Transactional
    public ProductResponse adjustStock(Long id, int delta) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        int newQty = nvl(p.getTotalQuantity()) + delta;
        if (newQty < 0) throw new BusinessException("Stock cannot be negative");
        p.setTotalQuantity(newQty);
        p.setUpdatedAt(OffsetDateTime.now());
        return toResponse(productRepository.save(p));
    }

    /* ======================= Stats (للـDashboard) ======================= */
    public InventoryStatsResponse stats() {
        List<Product> all = productRepository.findAll();
        long total = all.size();
        long out = all.stream().filter(p -> nvl(p.getTotalQuantity()) == 0).count();
        long low = all.stream().filter(p -> {
            int q = nvl(p.getTotalQuantity());
            int min = nvl(p.getMinimumStock());
            return q > 0 && q <= min;
        }).count();
        long in = total - out - low;

        BigDecimal value = productRepository.sumInventoryValue();

        return new InventoryStatsResponse(total, in, low, out, value);
    }

    /* ======================= Mapping & helpers ======================= */
    private ProductResponse toResponse(Product p) {
        InventoryStatus status;
        int q = nvl(p.getTotalQuantity());
        int min = nvl(p.getMinimumStock());
        if (q == 0) status = InventoryStatus.OUT_OF_STOCK;
        else if (q <= min) status = InventoryStatus.LOW_STOCK;
        else status = InventoryStatus.IN_STOCK;

        return new ProductResponse(
                p.getId(),
                p.getBrand().getId(),
                p.getName(),
                p.getCategory(),
                p.getSku(),
                p.getColors(),
                p.getSizes(),
                p.getCostPrice(),
                p.getSellingPrice(),
                p.getMinimumStock(),
                p.getTotalQuantity(),
                p.getTotalSell(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }

    private static int nvl(Integer v) {
        return v == null ? 0 : v;
    }
}
