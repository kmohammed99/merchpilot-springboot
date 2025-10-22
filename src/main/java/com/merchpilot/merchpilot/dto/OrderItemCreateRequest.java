package com.merchpilot.merchpilot.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record OrderItemCreateRequest(
        Long productId,
        String productName,
        String color,
        String size,
        @NotNull @Positive Integer quantity,
        @DecimalMin(value = "0.0") BigDecimal unitPrice
) {
    @AssertTrue(message = "Either productId or productName must be provided")
    public boolean isProductRefValid() {
        return productId != null || (productName != null && !productName.isBlank());
    }
}
