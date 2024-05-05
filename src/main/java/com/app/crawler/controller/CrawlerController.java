package com.app.crawler.controller;

import com.app.crawler.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crawler")
public class CrawlerController {
    @Autowired
    private ProductService productService;

    @GetMapping("/start")
    public String crawlAndSaveData() {
        if (productService.checkSaveData()) {
            return "The data has been saved to the database previously.";
        } else {
            productService.startCrawling();
            System.out.println("Crawling process started successfully.");
            return "Crawling process started successfully.";
        }
    }
}
