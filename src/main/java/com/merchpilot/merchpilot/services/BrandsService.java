// src/main/java/com/merchpilot/merchpilot/services/BrandsService.java
package com.merchpilot.merchpilot.services;

import com.merchpilot.merchpilot.dto.brand.*;
import com.merchpilot.merchpilot.entity.Brand;
import com.merchpilot.merchpilot.exception.BusinessException;
import com.merchpilot.merchpilot.exception.NotFoundException;
import com.merchpilot.merchpilot.repository.BrandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BrandsService {

    private final BrandRepository brandRepository;

    public BrandsService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Transactional
    public BrandResponse create(BrandRequest req) {
        if (brandRepository.existsByNameIgnoreCase(req.name()))
            throw new BusinessException("Brand name already exists");
        Brand b = new Brand();
        b.setName(req.name().trim());
        return BrandResponse.of(brandRepository.save(b));
    }

    @Transactional(readOnly = true)
    public Page<BrandResponse> list(String q, Pageable pageable) {
        Page<Brand> page = (q == null || q.isBlank())
                ? brandRepository.findAll(pageable)
                : brandRepository.findAll(pageable) // لو عايز Search فعلي: اعمل method مخصص بالـSpec/Example
                ;
        return page.map(BrandResponse::of);
    }

    @Transactional(readOnly = true)
    public BrandResponse get(Long id) {
        Brand b = brandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Brand not found"));
        return BrandResponse.of(b);
    }

    @Transactional
    public BrandResponse update(Long id, BrandUpdateRequest req) {
        Brand b = brandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Brand not found"));

        String newName = req.name().trim();
        if (!b.getName().equalsIgnoreCase(newName) && brandRepository.existsByNameIgnoreCase(newName)) {
            throw new BusinessException("Brand name already exists");
        }
        b.setName(newName);
        return BrandResponse.of(brandRepository.save(b));
    }

    @Transactional
    public void delete(Long id) {
        Brand b = brandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Brand not found"));
        brandRepository.delete(b);
    }
}
