package com.merchpilot.merchpilot.services;

import com.merchpilot.merchpilot.dto.OrderCreateRequest;
import com.merchpilot.merchpilot.dto.OrderItemCreateRequest;
import com.merchpilot.merchpilot.dto.OrderResponse;
import com.merchpilot.merchpilot.entity.*;
import com.merchpilot.merchpilot.entity.enums.OrderStatus;
import com.merchpilot.merchpilot.entity.enums.PaymentMethod;
import com.merchpilot.merchpilot.exception.BusinessException;
import com.merchpilot.merchpilot.exception.NotFoundException;
import com.merchpilot.merchpilot.repository.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdersService {

    private final OrdersRepository ordersRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final ShippingCompanyRepository shippingCompanyRepository;
    private final BrandRepository brandRepository;

    public OrdersService(OrdersRepository ordersRepository,
                         OrderItemRepository orderItemRepository,
                         ProductRepository productRepository,
                         ShippingCompanyRepository shippingCompanyRepository, BrandRepository brandRepository) {
        this.ordersRepository = ordersRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.shippingCompanyRepository = shippingCompanyRepository;
        this.brandRepository = brandRepository;
    }

    @Transactional
    public OrderResponse addOrder(@Valid OrderCreateRequest req) {
        if (req.items() == null || req.items().isEmpty())
            throw new BusinessException("Order must contain at least one item");

        Brand brand = brandRepository.findById(req.brandId())
                .orElseThrow(() -> new NotFoundException("Brand not found"));

        Order order = new Order();
        order.setBrand(brand);
        order.setCustomerName(req.customerName());
        order.setPhone(req.phone());
        order.setEmail(req.email());
        order.setGovernment(req.government());
        order.setAddress(req.address());

        try {
            order.setPaymentMethod(PaymentMethod.valueOf(req.paymentMethod().toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new BusinessException("Unsupported paymentMethod: " + req.paymentMethod());
        }

        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(OffsetDateTime.now());
        order.setDiscount(nvl(req.discount()));

        BigDecimal shippingFee = BigDecimal.ZERO;
        if (req.shippingCompanyId() != null) {
            ShippingCompany sc = shippingCompanyRepository.findById(req.shippingCompanyId())
                    .orElseThrow(() -> new NotFoundException("Shipping company not found"));
            order.setShippingCompany(sc);
            shippingFee = nvl(sc.getCompanyFee());
        }

        order = ordersRepository.save(order);

        // Process Items
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> entities = new ArrayList<>();
        List<OrderResponse.Line> lines = new ArrayList<>();

        for (OrderItemCreateRequest it : req.items()) {
            int qty = it.quantity();
            if (qty <= 0) throw new BusinessException("Quantity must be > 0");

            String snapshotName = it.productName();
            BigDecimal unit = nvl(it.unitPrice());

            Product p = null;
            if (it.productId() != null) {
                p = productRepository.findById(it.productId())
                        .orElseThrow(() -> new NotFoundException("Product not found: " + it.productId()));

                if (snapshotName == null || snapshotName.isBlank()) snapshotName = p.getName();
                if (unit.compareTo(BigDecimal.ZERO) == 0) unit = nvl(p.getSellingPrice());

                // خصم من المخزون
                int onHand = nvlInt(p.getTotalQuantity());
                if (onHand < qty) throw new BusinessException("Not enough stock for " + p.getSku());
                p.setTotalQuantity(onHand - qty);
                p.setTotalSell(nvlInt(p.getTotalSell()) + qty);
                productRepository.save(p);
            } else if (snapshotName == null || snapshotName.isBlank()) {
                throw new BusinessException("Either productId or productName must be provided");
            }

            BigDecimal lineTotal = unit.multiply(BigDecimal.valueOf(qty));
            total = total.add(lineTotal);

            OrderItem e = new OrderItem();
            e.setOrder(order);
            e.setProduct(p);
            e.setProductName(snapshotName);
            e.setColor(it.color());
            e.setSize(it.size());
            e.setQuantity(qty);
            e.setUnitPrice(unit);
            e.setLineTotal(lineTotal);
            entities.add(e);

            lines.add(new OrderResponse.Line(
                    it.productId(), snapshotName, it.color(), it.size(), qty, unit, lineTotal));
        }

        orderItemRepository.saveAll(entities);

        order.setTotalAmount(total);
        order.setNetAmount(total.subtract(nvl(order.getDiscount())).add(shippingFee));
        ordersRepository.save(order);

        return new OrderResponse(
                order.getId(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getNetAmount(),
                order.getOrderDate(),
                lines
        );
    }

    private static BigDecimal nvl(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    private static int nvlInt(Integer v) {
        return v == null ? 0 : v;
    }

}
