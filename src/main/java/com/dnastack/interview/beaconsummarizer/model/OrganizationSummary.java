package com.dnastack.interview.beaconsummarizer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationSummary {
    private String organization;
    private Integer beacons;
}
