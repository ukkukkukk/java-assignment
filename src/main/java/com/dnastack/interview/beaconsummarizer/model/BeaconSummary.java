package com.dnastack.interview.beaconsummarizer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeaconSummary {
    private List<OrganizationSummary> organizations;
    private Integer found;
    private Integer notFound;
    private Integer notApplicable;
    private Integer notResponding;
}
