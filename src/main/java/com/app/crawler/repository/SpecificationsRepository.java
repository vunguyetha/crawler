package com.app.crawler.repository;

import com.app.crawler.entity.Specifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecificationsRepository extends JpaRepository<Specifications, Long> {
    boolean existsByCode(String code);

    boolean existsByCodeAndValue(String code, String value);
}
