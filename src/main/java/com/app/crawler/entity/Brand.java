package com.app.crawler.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.Collection;
import java.util.Set;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tbl_brand")
public class Brand {
    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;


    @OneToMany(mappedBy="brand")
    @JsonIgnore
    private Set<Product> products;

    public Brand(JSONObject brandCrawled) {
        try {
            this.id = brandCrawled.get("id").toString();
            this.name = brandCrawled.get("name").toString();
            this.slug = brandCrawled.get("slug").toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
