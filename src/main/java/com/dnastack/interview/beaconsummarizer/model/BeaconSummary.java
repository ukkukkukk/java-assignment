package com.dnastack.interview.beaconsummarizer.model;

import lombok.Value;

import java.util.List;

@Value
public class BeaconSummary {
    private List<String> organizations;
    private List<String> beaconNames;

    public BeaconSummary (List<String> organizations, List<String> beaconNames)
    {
        this.organizations = organizations;
        this.beaconNames = beaconNames;
    }
}
