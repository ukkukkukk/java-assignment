package com.dnastack.interview.beaconsummarizer.client.beacon;

import lombok.Data;

import java.util.List;

@Data
public class Beacon {
    private String id;
    private String name;
    private String url;
    private String organization;
    private String description;
    private String homePage;
    private String email;
    private Boolean aggregator;
    private Boolean visible;
    private Boolean enabled;
    private List<String> supportedReferences;
}
