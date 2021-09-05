package com.dnastack.interview.beaconsummarizer;

import com.dnastack.interview.beaconsummarizer.model.BeaconSearchResults;
import com.dnastack.interview.beaconsummarizer.model.BeaconSummary;
import com.dnastack.interview.beaconsummarizer.model.OrganizationCountSummary;
import com.dnastack.interview.beaconsummarizer.service.IBeaconSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class BeaconSummaryController {

    @Value("${app.batch-size}")
    private Integer batchSize;

    @Autowired
    private IBeaconSearchService beaconSearchService;

    @GetMapping("/search")
    public BeaconSummary search(@RequestParam String ref,
                                @RequestParam String chrom,
                                @RequestParam String pos,
                                @RequestParam String allele,
                                @RequestParam String referenceAllele) throws Exception {
        //TODO: could not find referenceAllele in APIs

        BeaconSearchResults beaconSearchResults = beaconSearchService.searchAllBeacons(ref, chrom, pos, allele, batchSize);

        Map<String, OrganizationCountSummary> countsByOrganization = SummarizeResultsHelper.findBeaconCountsByOrganization(beaconSearchResults.getBeaconDetails());


        return SummarizeResultsHelper.analyzeOrganizationCountSummary(beaconSearchResults.getBeaconDetails(), countsByOrganization, beaconSearchResults.getBeaconIds(), beaconSearchResults.getOrganizationNames());
    }
}
