package com.app.crawler.dto;

import com.app.crawler.entity.Categories;
import com.app.crawler.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ProductViewDto {
    private String id;
    private String name;
    private String shortUrl;
    private Integer originalPrice;
    private Integer price;
    private String ratingAverage;
    private String shortDescription;
    private String quantitySold;
    private String thumbnailUrl;
    private String inventoryType;
    private String category;
    private String brand;
    private String seller;

    public ProductViewDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.shortUrl = product.getShortUrl();
        this.shortDescription = product.getDescription();
        this.originalPrice = product.getOriginalPrice();
        this.price = product.getPrice();
        this.ratingAverage = product.getRatingAverage();
        this.quantitySold = product.getQuantitySold();
        this.thumbnailUrl = product.getThumbnailUrl();
        this.inventoryType = product.getInventoryType();
        this.category = product.getCategories().stream()
                .map(Categories::getName)
                .toList()
                .toString();
        this.brand = product.getBrand().getName();
        this.seller = product.getSeller().getName();
    }
}
