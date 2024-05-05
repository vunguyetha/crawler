package com.app.crawler.repository;

import com.app.crawler.entity.Brand;
import com.app.crawler.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    boolean existsById(String id);
}
