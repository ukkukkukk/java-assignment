package com.dnastack.interview.beaconsummarizer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;

import java.util.List;

@Value
public class OrganizationSummary {
    private String organization;
    private Integer beacons;

}
