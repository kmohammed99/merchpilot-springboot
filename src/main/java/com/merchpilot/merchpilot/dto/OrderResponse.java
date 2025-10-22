// dto/OrderResponse.java
package com.merchpilot.merchpilot.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String status,
        BigDecimal totalAmount,
        BigDecimal netAmount,
        OffsetDateTime orderDate,
        List<Line> items
) {
    public record Line(Long productId, String name, String color, String size, int qty, BigDecimal unitPrice,
                       BigDecimal lineTotal) {
    }
}
