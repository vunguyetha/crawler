package com.app.crawler.repository;

import com.app.crawler.entity.Brand;
import com.app.crawler.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    boolean existsById(String id);

    List<Brand> findAllByOrderByNameAsc();
}
