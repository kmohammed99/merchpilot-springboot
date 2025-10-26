// src/main/java/com/merchpilot/merchpilot/controllers/BrandsController.java
package com.merchpilot.merchpilot.controller;

import com.merchpilot.merchpilot.common.web.RequestTransaction;
import com.merchpilot.merchpilot.common.web.ResponseTransaction;
import com.merchpilot.merchpilot.dto.brand.*;
import com.merchpilot.merchpilot.services.BrandsService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brands")
public class BrandsController {

    private final BrandsService brandsService;

    public BrandsController(BrandsService brandsService) {
        this.brandsService = brandsService;
    }

    @PostMapping
    public ResponseTransaction create(@Valid @RequestBody RequestTransaction wrapper) {
        BrandRequest req = (BrandRequest) wrapper.getRequestBody();
        return ResponseTransaction.buildSuccessResponse(brandsService.create(req));
    }

    @GetMapping
    public ResponseTransaction list(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<BrandResponse> result = brandsService.list(q, PageRequest.of(page, size));
        return ResponseTransaction.buildSuccessResponse(result);
    }

    @GetMapping("/{id}")
    public ResponseTransaction get(@PathVariable Long id) {
        return ResponseTransaction.buildSuccessResponse(brandsService.get(id));
    }

    @PutMapping("/{id}")
    public ResponseTransaction update(@PathVariable Long id,
                                      @Valid @RequestBody RequestTransaction wrapper) {
        BrandUpdateRequest req = (BrandUpdateRequest) wrapper.getRequestBody();
        return ResponseTransaction.buildSuccessResponse(brandsService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseTransaction delete(@PathVariable Long id) {
        brandsService.delete(id);
        return ResponseTransaction.buildSuccessResponse(null, "Deleted");
    }
}
