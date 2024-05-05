package com.app.crawler.response;

import com.app.crawler.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ProductList {
    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private int from;
    private int to;
    private List<ProductDto> data;
}
