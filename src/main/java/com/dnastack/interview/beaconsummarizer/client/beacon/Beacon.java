package com.dnastack.interview.beaconsummarizer.client.beacon;

import lombok.Data;

import java.time.LocalDate;
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
    private boolean aggregator;
    private boolean visible;
    private boolean enabled;
    private List<String> supportedReferences;

    public String getName ()
    {
        return this.name;
    }
}
