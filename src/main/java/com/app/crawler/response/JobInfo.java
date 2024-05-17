package com.app.crawler.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class JobInfo {
    private String jobTitle;
    private String srcImg;
    private String href;
}
