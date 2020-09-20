package com.dnastack.interview.beaconsummarizer.model;

import lombok.Value;

import java.util.List;

@Value
public class BeaconSummary {
    private List<OrganizationSummary> organizations;
    private Integer found;
    private Integer notFound;
    private Integer notApplicable;
    private Integer notResponding;


    public BeaconSummary(List<OrganizationSummary> organizationSummaries, int found, int notFound, int notApplicable, int notResponding) {
        this.organizations = organizationSummaries;
        this.found = found;
        this.notFound = notFound;
        this.notApplicable = notApplicable;
        this.notResponding = notResponding;
    }
}
