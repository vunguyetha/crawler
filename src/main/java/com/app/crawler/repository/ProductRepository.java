package com.app.crawler.repository;

import com.app.crawler.entity.Brand;
import com.app.crawler.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsById(String id);

    Page<Product> findAllByOrderByIdAsc(Pageable pageable);

    @Query(value = "SELECT * FROM tbl_product p " +
            "WHERE p.name LIKE CONCAT('%', :name , '%') " +
            "AND p.price >= :minBudget " +
            "AND p.price <= :maxBudget " +
            "AND p.brand_id IN :brandIds " +
            "ORDER BY p.name ASC",
            nativeQuery = true)
    List<Product> findProductsByNameAndPriceAndBrandIds(@Param("name") String name,
                                                        @Param("minBudget") Integer minBudget,
                                                        @Param("maxBudget") Integer maxBudget,
                                                        @Param("brandIds") List<String> brandIds);

    @Query(value = "SELECT * FROM tbl_product p " +
            "WHERE p.name LIKE CONCAT('%', :name , '%') " +
            "AND p.price >= :minBudget " +
            "AND p.price <= :maxBudget " +
            "ORDER BY p.name ASC",
            nativeQuery = true)
    List<Product> findProductsByNameAndPrice(@Param("name") String name,
                                             @Param("minBudget") Integer minBudget,
                                             @Param("maxBudget") Integer maxBudget);
}
