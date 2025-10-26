package com.merchpilot.merchpilot.dto.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record ProductUpdateRequest(
        Long brandId,
        @Size(max = 200) String name,
        @Size(max = 120) String category,
        @Size(max = 120) String sku,

        @DecimalMin(value = "0.0") BigDecimal costPrice,
        @DecimalMin(value = "0.0") BigDecimal sellingPrice,

        @Min(0) Integer minimumStock,
        @Min(0) Integer totalQuantity
) {
}
