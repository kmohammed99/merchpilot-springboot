package com.merchpilot.merchpilot.dto.orderDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record OrderCreateRequest(
        @NotNull Long brandId,
        @NotBlank String customerName,
        @NotBlank String phone,
        @Email String email,
        String government,
        String address,
        @NotBlank String paymentMethod,         // CASH / CARD / ...
        Long shippingCompanyId,
        @DecimalMin(value = "0.0") BigDecimal discount,
        @NotEmpty List<@Valid OrderItemCreateRequest> items
) {
}
