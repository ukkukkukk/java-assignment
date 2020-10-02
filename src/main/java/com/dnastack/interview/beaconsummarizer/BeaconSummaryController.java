package com.dnastack.interview.beaconsummarizer;

import com.dnastack.interview.beaconsummarizer.client.beacon.Beacon;
import com.dnastack.interview.beaconsummarizer.client.beacon.BeaconDetail;
import com.dnastack.interview.beaconsummarizer.client.beacon.Organization;
import com.dnastack.interview.beaconsummarizer.model.BeaconSearchResults;
import com.dnastack.interview.beaconsummarizer.model.BeaconSummary;
import com.dnastack.interview.beaconsummarizer.model.OrganizationCountSummary;
import com.dnastack.interview.beaconsummarizer.service.BeaconClientService;
import com.dnastack.interview.beaconsummarizer.service.IBeaconClientService;
import com.dnastack.interview.beaconsummarizer.service.IBeaconSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

@RestController

public class BeaconSummaryController {

    private static final int BATCH_SIZE = 3;

    @Autowired
    private IBeaconSearchService beaconSearchService;

    @GetMapping("/search")
    public BeaconSummary search(@RequestParam String ref,
                                @RequestParam String chrom,
                                @RequestParam String pos,
                                @RequestParam String allele,
                                @RequestParam String referenceAllele) throws Exception {
        //TODO: could not find referenceAllele in APIs

        BeaconSearchResults beaconSearchResults = beaconSearchService.searchAllBeacons(ref, chrom, pos, allele, BATCH_SIZE);

        Map<String, OrganizationCountSummary> countsByOrganization = SummarizeResultsHelper.findBeaconCountsByOrganization(beaconSearchResults.getBeaconDetails());


        return SummarizeResultsHelper.analyzeOrganizationCountSummary(beaconSearchResults.getBeaconDetails(), countsByOrganization, beaconSearchResults.getBeaconIds(), beaconSearchResults.getOrganizationNames());
    }


}
