package com.app.crawler.controller;

import com.app.crawler.dto.ProductViewDto;
import com.app.crawler.entity.Brand;
import com.app.crawler.request.ProductSearchRequest;
import com.app.crawler.service.BrandService;
import com.app.crawler.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    private static final int MIN_BUDGET = 1000;

    private static final int MAX_BUDGET = 1000000000;

    @GetMapping("/form")
    public String showSearchForm(@RequestParam(defaultValue = "1") final Integer pageNumber,
                                 @RequestParam(defaultValue = "10") final Integer size,
                                 Model model) {
        Page<ProductViewDto> productViewDtos = productService.findAllByOrderByIdAsc(PageRequest.of(pageNumber, size));
        model.addAttribute("products", productViewDtos);

        ProductSearchRequest product = new ProductSearchRequest();
        model.addAttribute("product", product);

        List<Brand> brands = brandService.findAllByOrderByNameAsc();
        model.addAttribute("brands", brands);

        return "searchForm";
    }

    @PostMapping("/result")
    public String search(@Valid @ModelAttribute("product") ProductSearchRequest product,
                         BindingResult result,
                         Model model) {
        List<Brand> brands = brandService.findAllByOrderByNameAsc();
        model.addAttribute("brands", brands);
        if (product.getMinBudget() == null) product.setMinBudget(MIN_BUDGET);
        if (product.getMaxBudget() == null) product.setMaxBudget(MAX_BUDGET);
        else if (product.getMaxBudget() < product.getMinBudget()) {
            result.rejectValue("minBudget", null,
                    "Min must less than Max");
        }

        if (result.hasErrors()) {
            model.addAttribute("product", product);
            return "searchForm";
        }

        List<ProductViewDto> results = productService.findPageProductsBySearchForm(product);
        model.addAttribute("products", results);
        model.addAttribute("key", product);

        return "searchResult";
    }

}
