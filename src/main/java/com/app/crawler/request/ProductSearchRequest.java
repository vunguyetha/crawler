package com.app.crawler.request;

import com.app.crawler.entity.Brand;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ProductSearchRequest {
    private String name;
    @Min(value = 1000, message = "Price should start 1000VND")
    private Integer minBudget;
    @Min(value = 1000, message = "Price should start 1000VND")
    private Integer maxBudget;
    private List<Brand> brands;
}
