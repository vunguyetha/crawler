package com.app.crawler.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.JsonObject;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tbl_categories")
public class Categories {
    @Id
    private String id;

    @Column(name = "name", columnDefinition = "LONGTEXT")
    @Lob
    private String name;

    @Column(name = "url", columnDefinition = "LONGTEXT")
    @Lob
    private String url;

    @JsonIgnore
    @ManyToMany(mappedBy = "categories")
    @EqualsAndHashCode.Exclude
    private Collection<Product> products;

    public Categories(JSONObject categoryCrawled) {
        try {
            this.id = categoryCrawled.get("category_id").toString();
            this.name = categoryCrawled.get("name").toString();
            this.url = categoryCrawled.get("url").toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
