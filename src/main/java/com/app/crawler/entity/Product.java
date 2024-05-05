package com.app.crawler.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.Collection;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tbl_product")
public class Product {

    @Id
    private String id;

    @Column(name = "name", columnDefinition = "LONGTEXT")
    @Lob
    private String name;

    @Column(name = "short_url")
    private String shortUrl;

    @Column(name = "original_price")
    private Integer originalPrice;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "price")
    private Integer price;

    @Column(name = "rating_average")
    private String ratingAverage;

    @Column(name = "review_count")
    private String reviewCount;
    @Column(name = "inventory_status")
    private String inventoryStatus;

    @Column(name = "inventory_type")
    private String inventoryType;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    @Lob
    private String description;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "quantity_sold")
    private String quantitySold;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "has_buynow")
    private String hasBuyNow;

    @Column(name = "day_ago_created")
    private String dayAgoCreated;

    @ManyToMany(cascade = CascadeType.MERGE , fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(name = "product_categories",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    private Collection<Categories> categories;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "brand_id", nullable=false)
    private Brand brand;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "seller_id", nullable=false)
    private Seller seller;

    @ManyToMany(cascade = CascadeType.MERGE , fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinTable(name = "product_specifications",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "specifications_id")}
    )
    private Collection<Specifications> specifications;

    public Product(JSONObject productCrawled) {
        try {
            this.id = productCrawled.get("id").toString();
            this.name = productCrawled.get("name").toString() ;
            this.shortUrl = productCrawled.get("short_url").toString() ;
            this.originalPrice = Integer.valueOf(productCrawled.get("original_price").toString());
            this.discount = Integer.valueOf(productCrawled.get("discount").toString());
            this.price = Integer.valueOf(productCrawled.get("price").toString());
            this.ratingAverage = productCrawled.get("rating_average").toString();
            this.reviewCount = productCrawled.get("review_count").toString();
            this.inventoryStatus = productCrawled.get("inventory_status").toString();
            this.inventoryType = productCrawled.get("inventory_type").toString();
            this.description = productCrawled.get("description").toString();
            this.shortDescription = productCrawled.get("short_description").toString();
            this.quantitySold = productCrawled.has("all_time_quantity_sold") ? productCrawled.get("all_time_quantity_sold").toString() : "0";
            this.thumbnailUrl = productCrawled.get("thumbnail_url").toString();
            this.hasBuyNow = productCrawled.get("has_buynow").toString();
            this.dayAgoCreated = productCrawled.get("day_ago_created").toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
