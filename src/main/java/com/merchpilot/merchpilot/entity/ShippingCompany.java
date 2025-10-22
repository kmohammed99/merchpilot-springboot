package com.merchpilot.merchpilot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "shipping_companies",
        uniqueConstraints = @UniqueConstraint(name = "uq_shipping_company_brand_name", columnNames = {"brand_id", "name"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @Column(nullable = false, length = 150)
    private String name;
    @Column(name = "company_fee", nullable = false, precision = 12, scale = 2)
    private BigDecimal companyFee;
}
