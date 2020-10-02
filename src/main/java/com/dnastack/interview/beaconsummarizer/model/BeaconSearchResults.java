package com.dnastack.interview.beaconsummarizer.model;

import com.dnastack.interview.beaconsummarizer.client.beacon.Beacon;
import com.dnastack.interview.beaconsummarizer.client.beacon.BeaconDetail;
import com.dnastack.interview.beaconsummarizer.client.beacon.Organization;
import lombok.Value;

import java.util.List;

@Value
public class BeaconSearchResults {
    private List<String> organizationNames;
    private List<String> beaconIds;
    private List<BeaconDetail> beaconDetails;

}


