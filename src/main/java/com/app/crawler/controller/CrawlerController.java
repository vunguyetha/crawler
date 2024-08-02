package com.app.crawler.controller;

import com.app.crawler.response.JobInfo;
import com.app.crawler.service.LinkedinService;
import com.app.crawler.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/crawler")
public class CrawlerController {
    @Autowired
    private ProductService productService;

    @Autowired
    private LinkedinService linkedinService;

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

    @GetMapping("/start-crawl-with-linkedin")
    public ModelAndView crawlFromLinkedin(@RequestParam(name = "keywords") String keywords) {
        ModelAndView resultView = new ModelAndView("linkedinResult");
        List<JobInfo> jobInfos = linkedinService.startCrawlingFromLinkedIn(keywords);
        resultView.addObject("results", jobInfos);
        return resultView;
    }
}
