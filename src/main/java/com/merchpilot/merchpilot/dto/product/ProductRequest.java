// ProductRequest.java
package com.merchpilot.merchpilot.dto.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record ProductRequest(
        @NotNull Long brandId,
        @NotBlank @Size(max = 200) String name,
        @NotBlank @Size(max = 120) String category,
        @NotBlank @Size(max = 120) String sku,

        @NotNull @Size(min = 0) List<@NotBlank @Size(max = 60) String> colors,
        @NotNull @Size(min = 0) List<@NotBlank @Size(max = 60) String> sizes,

        @NotNull @DecimalMin(value = "0.0") BigDecimal costPrice,
        @NotNull @DecimalMin(value = "0.0") BigDecimal sellingPrice,
        @NotNull @Min(0) Integer minimumStock,
        @NotNull @Min(0) Integer totalQuantity
) {
}
