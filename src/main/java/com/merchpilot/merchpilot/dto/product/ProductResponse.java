package com.merchpilot.merchpilot.dto.product;

import com.merchpilot.merchpilot.entity.Product;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record ProductResponse(
        Long id,
        Long brandId,
        String name,
        String category,
        String sku,
        List<String> colors,
        List<String> sizes,
        BigDecimal costPrice,
        BigDecimal sellingPrice,
        Integer minimumStock,
        Integer totalQuantity,
        Integer totalSell,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
    public static ProductResponse of(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getBrand() != null ? p.getBrand().getId() : null,
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
}
