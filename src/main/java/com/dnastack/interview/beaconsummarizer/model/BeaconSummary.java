package com.dnastack.interview.beaconsummarizer.model;

import lombok.Value;

import java.util.List;

@Value
public class BeaconSummary {
    private List<String> organizationNames;
    private List<String> beaconIds;

    public BeaconSummary(List<String> organizationNames, List<String> beaconIds) {
        this.organizationNames = organizationNames;
        this.beaconIds = beaconIds;
    }
}
