package com.dnastack.interview.beaconsummarizer;

import com.dnastack.interview.beaconsummarizer.client.beacon.Beacon;
import com.dnastack.interview.beaconsummarizer.client.beacon.BeaconClient;
import com.dnastack.interview.beaconsummarizer.client.beacon.BeaconDetail;
import com.dnastack.interview.beaconsummarizer.client.beacon.Organization;
import com.dnastack.interview.beaconsummarizer.model.BeaconSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

@RestController

public class BeaconSummaryController {

    static final int BATCH_SIZE = 3;

    @Autowired
    private BeaconLookupService beaconLookupService;

    @GetMapping("/search")
    public BeaconSummary search(@RequestParam String ref,
                                @RequestParam String chrom,
                                @RequestParam String pos,
                                @RequestParam String allele,
                                @RequestParam String referenceAllele) throws Exception {


        //get organizations and beacons in parallel
        CompletableFuture<List<Organization>> organizationResults = beaconLookupService.getOrganizations();
        CompletableFuture<List<Beacon>> beaconResults = beaconLookupService.getBeacons();

        //wait for above async calls to complete
        CompletableFuture.allOf(organizationResults, beaconResults).join();

        List<String> organizationNames = organizationResults.get().stream().map(Organization::getName).collect(toList());

        List<String> beaconIds = beaconResults.get().stream().map(Beacon::getId).collect(toList());

        //TODO: handle case where orgNames or beaconNames is null

        //get beacon details
        List<BeaconDetail> beaconDetails = getBeaconDetailsInBatches(beaconIds, ref, chrom, pos, allele, referenceAllele);

        System.out.println("Retrieved beacon details: " + beaconDetails.size());

        return new BeaconSummary(organizationNames, beaconIds);
    }

    private List<BeaconDetail> getBeaconDetailsInBatches(List<String> beaconIds, String ref, String chrom, String pos, String allele, String referenceAllele) throws Exception {
        int batchCount = 0;
        List<String> beaconNamesCurrentBatch = new ArrayList<String>();
        List<CompletableFuture<List<BeaconDetail>>> tasks = new ArrayList<CompletableFuture<List<BeaconDetail>>>();

        System.out.println("Getting beacon details for: " + beaconIds.size());

        for (int i = 0; i < beaconIds.size(); i++) {
            beaconNamesCurrentBatch.add(beaconIds.get(i));
            batchCount++;

            //if we have reached 3 for the current batch, or last record
            if (batchCount == BATCH_SIZE || i == beaconIds.size() - 1) {
                //create copy for thread
                List<String> beaconNamesForThread = new ArrayList<String>(beaconNamesCurrentBatch);

                tasks.add(beaconLookupService.getBeaconDetails(ref, chrom, pos, allele, beaconNamesForThread));

                batchCount = 0;
                beaconNamesCurrentBatch.removeAll(beaconNamesCurrentBatch);
            }
        }

        //wait for above async calls to complete
        CompletableFuture.allOf(tasks.toArray(new CompletableFuture[tasks.size()])).join();

        List<BeaconDetail> beaconDetails = new ArrayList<BeaconDetail>();
        //append to final list
        for (CompletableFuture<List<BeaconDetail>> task : tasks) {
            List<BeaconDetail> beaconDetailResults = task.get();

            if (beaconDetailResults != null)
                beaconDetails.addAll(beaconDetailResults);
        }

        return beaconDetails;

    }


}
