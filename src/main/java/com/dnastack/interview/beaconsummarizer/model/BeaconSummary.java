package com.dnastack.interview.beaconsummarizer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Value;

import java.util.List;

@Value
public class BeaconSummary {
    private List<OrganizationSummary> organizations;
    private Integer found;
    private Integer notFound;
    private Integer notApplicable;
    private Integer notResponding;


}
