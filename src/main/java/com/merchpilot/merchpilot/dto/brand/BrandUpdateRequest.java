// src/main/java/com/merchpilot/merchpilot/dto/brand/BrandUpdateRequest.java
package com.merchpilot.merchpilot.dto.brand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BrandUpdateRequest(
        @NotBlank @Size(min = 2, max = 120)
        String name
) {}
