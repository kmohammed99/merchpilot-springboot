package com.merchpilot.merchpilot.repository;

import com.merchpilot.merchpilot.entity.Product;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    @Query("""
            select p from Product p
            where (:q is null or lower(p.name) like lower(concat('%',:q,'%'))
                        or lower(p.category) like lower(concat('%',:q,'%'))
                        or lower(p.sku) like lower(concat('%',:q,'%')))
              and (:brandId is null or p.brand.id = :brandId)
            """)
    Page<Product> search(@Param("q") String q,
                         @Param("brandId") Long brandId,
                         Pageable pageable);

    // إجمالي قيمة المخزون = sum(price * qty)
    @Query("select coalesce(sum(p.sellingPrice * p.totalQuantity), 0) from Product p")
    BigDecimal sumInventoryValue();

}
