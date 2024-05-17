package com.app.crawler.service;

import com.app.crawler.response.JobInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LinkedinService {
    public List<JobInfo> startCrawlingFromLinkedIn(String keywords) {
        try {

            String url = "https://www.linkedin.com/jobs/search/?currentJobId=3903663421&geoId=90010186&keywords=" + keywords + "&location=Hanoi%20Capital%20Region&origin=JOB_SEARCH_PAGE_LOCATION_AUTOCOMPLETE&refresh=true";
            Document doc = Jsoup.connect(url).get();

            Elements ul = doc.select("ul.jobs-search__results-list");
            Elements li = ul.select("li");
            List<JobInfo> results = new ArrayList<>();

            int i = 1;
            for (Element item : li) {
                String jobTitle = item.getElementsByClass("base-search-card__title").text();
                String srcImg = item.select("img.artdeco-entity-image").attr("data-delayed-url");
                Element anchorTag = doc.selectFirst("div.base-search-card__info > h4.base-search-card__subtitle > a");
                String href = anchorTag.attr("href");

                JobInfo result = new JobInfo(jobTitle, srcImg, href);
                results.add(result);
            }
            System.out.println(results);
            return results;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
