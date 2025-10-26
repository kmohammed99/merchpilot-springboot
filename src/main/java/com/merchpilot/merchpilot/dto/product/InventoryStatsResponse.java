package com.merchpilot.merchpilot.dto.product;

import java.math.BigDecimal;

public record InventoryStatsResponse(
        long totalProducts,
        long inStock,
        long lowStock,
        long outOfStock,
        BigDecimal totalInventoryValue // sum(sellingPrice * totalQuantity)
) {
}