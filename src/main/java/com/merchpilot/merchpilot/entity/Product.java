package com.merchpilot.merchpilot.entity;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Column(nullable = false, length = 200)
    private String name;
    @Column(nullable = false, length = 120)
    private String category;
    @Column(nullable = false, unique = true, length = 120)
    private String sku;

    @Type(ListArrayType.class)
    @Column(name = "colors", columnDefinition = "text[]", nullable = false)
    private List<String> colors;

    @Type(ListArrayType.class)
    @Column(name = "sizes", columnDefinition = "text[]", nullable = false)
    private List<String> sizes;

    @Column(name = "cost_price", nullable = false)
    private BigDecimal costPrice;
    @Column(name = "selling_price", nullable = false)
    private BigDecimal sellingPrice;
    @Column(name = "minimum_stock", nullable = false)
    private Integer minimumStock;
    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity;
    @Column(name = "total_sell", nullable = false)
    private Integer totalSell;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
