package com.dnastack.interview.beaconsummarizer.client.beacon;

import lombok.Data;

@Data
public class BeaconDetail {
    private Beacon beacon;
    private Query query;
    private Boolean response;
    private String frequency;
    private String externalUrl;


}
