package com.merchpilot.merchpilot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "shipping_company_cities",
        uniqueConstraints = @UniqueConstraint(name = "uq_company_city", columnNames = {"company_id", "city"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingCompanyCity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private ShippingCompany company;

    @Column(nullable = false, length = 120)
    private String city;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;
}


//    وحدّث ShippingCompany لو عايز تجمع المدن:
//    // inside ShippingCompany.java
//    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
//    private java.util.List<ShippingCompanyCity> cities = new java.util.ArrayList<>();
//

