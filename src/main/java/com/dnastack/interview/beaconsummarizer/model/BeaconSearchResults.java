package com.dnastack.interview.beaconsummarizer.model;

import com.dnastack.interview.beaconsummarizer.client.beacon.BeaconDetail;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BeaconSearchResults {
    private List<String> organizationNames;
    private List<String> beaconIds;
    private List<BeaconDetail> beaconDetails;
}
