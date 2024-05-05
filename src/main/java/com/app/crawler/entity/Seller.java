package com.app.crawler.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.Set;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tbl_seller")
public class Seller {
    @Id
    private String id;

    @Column(name = "name", columnDefinition = "LONGTEXT")
    @Lob
    private String name;

    @Column(name = "sku")
    private String sku;

    @Column(name = "link", columnDefinition = "LONGTEXT")
    @Lob
    private String link;

    @Column(name = "is_best_store")
    private String isBestStore;


    @OneToMany(mappedBy="seller")
    @JsonIgnore
    private Set<Product> products;

    public Seller(JSONObject sellerCrawled) {
        try {
            this.id = sellerCrawled.get("store_id").toString();
            this.name = sellerCrawled.get("name").toString();
            this.sku = sellerCrawled.get("sku").toString();
            this.link = sellerCrawled.get("link").toString();
            this.isBestStore = sellerCrawled.get("is_best_store").toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
