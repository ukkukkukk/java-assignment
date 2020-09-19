package com.dnastack.interview.beaconsummarizer;

import com.dnastack.interview.beaconsummarizer.client.beacon.Beacon;
import com.dnastack.interview.beaconsummarizer.client.beacon.BeaconClient;
import com.dnastack.interview.beaconsummarizer.client.beacon.Organization;
import com.dnastack.interview.beaconsummarizer.model.BeaconSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

@RestController
public class BeaconSummaryController {

    @Autowired
    private BeaconClient beaconClient;

    @GetMapping("/search")
    public BeaconSummary search(@RequestParam String ref,
                                @RequestParam String chrom,
                                @RequestParam String pos,
                                @RequestParam String allele,
                                @RequestParam String referenceAllele) throws Exception {


        BeaconLookupService service = new BeaconLookupService(beaconClient);

        CompletableFuture<List<String>> orgNamesResult = service.getOrganizations();
        CompletableFuture<List<String>> beaconsResult = service.getBeacons();

        List<CompletableFuture> tasks = new ArrayList<CompletableFuture>();

        tasks.add(orgNamesResult);
        tasks.add(beaconsResult);

        CompletableFuture.allOf(tasks.toArray(new CompletableFuture[tasks.size()])).join();

        List<String> orgNames = orgNamesResult.get();
        List<String> beaconNames = beaconsResult.get();


        return new BeaconSummary(orgNames, beaconNames);
    }
}
