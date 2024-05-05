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
@Table(name = "tbl_specifications")
public class Specifications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", columnDefinition = "LONGTEXT")
    @Lob
    private String code;

    @Column(name = "name", columnDefinition = "LONGTEXT")
    @Lob
    private String name;

    @Column(name = "value", columnDefinition = "LONGTEXT")
    @Lob
    private String value;

    @JsonIgnore
    @ManyToMany(mappedBy = "specifications")
    @EqualsAndHashCode.Exclude
    private Collection<Product> products;

    public Specifications(JSONObject specificationsCrawled) {
        try {
            this.name = specificationsCrawled.get("name").toString();
            this.code = specificationsCrawled.get("code").toString();
            this.value = specificationsCrawled.get("value").toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
