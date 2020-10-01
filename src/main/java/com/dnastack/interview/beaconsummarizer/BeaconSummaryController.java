package com.dnastack.interview.beaconsummarizer;

import com.dnastack.interview.beaconsummarizer.client.beacon.Beacon;
import com.dnastack.interview.beaconsummarizer.client.beacon.BeaconDetail;
import com.dnastack.interview.beaconsummarizer.client.beacon.Organization;
import com.dnastack.interview.beaconsummarizer.model.BeaconSummary;
import com.dnastack.interview.beaconsummarizer.model.OrganizationCountSummary;
import com.dnastack.interview.beaconsummarizer.service.BeaconService;
import com.dnastack.interview.beaconsummarizer.service.IBeaconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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

    @Autowired
    @Qualifier("beaconLookUpService")
    private IBeaconService beaconService;




    @GetMapping("/search")
    public BeaconSummary search(@RequestParam(required = false) String ref,
                                @RequestParam String chrom,
                                @RequestParam String pos,
                                @RequestParam String allele,
                                @RequestParam String referenceAllele , @RequestHeader(required = false) Map<String, String> headers ) throws Exception {
        //TODO: could not find referenceAllele in APIs

        //get organizations and beacons in parallel
        CompletableFuture<List<Organization>> organizationResults = beaconService.getOrganizations();
        CompletableFuture<List<Beacon>> beaconResults = beaconService.getBeacons();

        //wait for above async calls to complete
        CompletableFuture.allOf(organizationResults, beaconResults).join();

        List<Organization> organizations = organizationResults.get();
        List<Beacon> beacons = beaconResults.get();

        if (beacons == null || organizations == null) {
            throw new ServerError("Internal server error ", new Error());
        }

        List<String> organizationNames = organizations.stream().map(Organization::getName).collect(toList());

        List<String> beaconIds = beacons.stream().map(Beacon::getId).collect(toList());


        //get beacon details
        List<BeaconDetail> beaconDetails = beaconService.getBeaconDetailsInBatches(beaconIds, ref, chrom, pos, allele).get();

        System.out.println("Retrieved beacon details: " + beaconDetails.size());

        Map<String, OrganizationCountSummary> countsByOrganization = SummarizeResultsHelper.findBeaconCountsByOrganization(beaconDetails);


        return SummarizeResultsHelper.analyzeOrganizationCountSummary(beaconDetails, countsByOrganization, beaconIds, organizationNames);
    }




}
