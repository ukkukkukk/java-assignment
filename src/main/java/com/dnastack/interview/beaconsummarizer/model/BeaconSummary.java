package com.dnastack.interview.beaconsummarizer.model;

import lombok.Value;

import java.util.List;

@Value
public class BeaconSummary {
    private List<String> organizations;

    public BeaconSummary (List<String> organizations)
    {
        this.organizations = organizations;
    }
}
