package com.merchpilot.merchpilot.controller;

import com.merchpilot.merchpilot.common.web.RequestTransaction;
import com.merchpilot.merchpilot.common.web.ResponseTransaction;
import com.merchpilot.merchpilot.dto.orderDTO.OrderCreateRequest;
import com.merchpilot.merchpilot.dto.orderDTO.OrderResponse;
import com.merchpilot.merchpilot.services.OrdersService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping
    public ResponseEntity<ResponseTransaction> create(
            @RequestBody @Valid RequestTransaction request) {

        if (!(request.getRequestBody() instanceof OrderCreateRequest req)) {
            return ResponseEntity.badRequest()
                    .body(ResponseTransaction.buildFailure(400, "Invalid request body", null));
        }

        OrderResponse result = ordersService.addOrder(req);
        return ResponseEntity.ok(ResponseTransaction.buildSuccessResponse(result, "Order created"));
    }


}
