package com.app.crawler.service;

import com.app.crawler.entity.Brand;
import com.app.crawler.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandRepository brandRepository;

    public List<Brand> findAllByOrderByNameAsc() {
        return brandRepository.findAllByOrderByNameAsc();
    }
}
