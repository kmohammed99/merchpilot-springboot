package com.merchpilot.merchpilot.dto.brand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BrandRequest(
        @NotBlank @Size(min = 2, max = 120)
        String name
) {}
