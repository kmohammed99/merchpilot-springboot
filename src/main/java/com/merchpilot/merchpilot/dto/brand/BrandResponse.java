// src/main/java/com/merchpilot/merchpilot/dto/brand/BrandResponse.java
package com.merchpilot.merchpilot.dto.brand;

public record BrandResponse(Long id, String name) {
    public static BrandResponse of(com.merchpilot.merchpilot.entity.Brand b) {
        return new BrandResponse(b.getId(), b.getName());
    }
}
