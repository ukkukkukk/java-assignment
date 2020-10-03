package com.dnastack.interview.beaconsummarizer.service;

import com.dnastack.interview.beaconsummarizer.client.beacon.Beacon;
import com.dnastack.interview.beaconsummarizer.client.beacon.BeaconDetail;
import com.dnastack.interview.beaconsummarizer.client.beacon.Organization;
import com.dnastack.interview.beaconsummarizer.model.BeaconSearchResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.ServerError;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

@Service
public class BeaconSearchService implements IBeaconSearchService {

    @Autowired
    IBeaconClientService beaconClientService;

    @Override
    public BeaconSearchResults searchAllBeacons(String reference, String chromosome, String position, String allele, int batchSize) throws Exception {
        //get organizations and beacons in parallel
        CompletableFuture<List<Organization>> organizationResults = beaconClientService.getOrganizations();
        CompletableFuture<List<Beacon>> beaconResults = beaconClientService.getBeacons();

        //wait for above async calls to complete
        CompletableFuture.allOf(organizationResults, beaconResults).join();

        List<Organization> organizations = organizationResults.get();
        List<Beacon> beacons = beaconResults.get();

        if (beacons == null || organizations == null) {
            throw new ServerException("Internal server error. ");
        }

        List<String> organizationNames = organizations.stream().map(Organization::getName).collect(toList());

        List<String> beaconIds = beacons.stream().map(Beacon::getId).collect(toList());


        //get beacon details
        List<BeaconDetail> beaconDetails = getBeaconDetailsInBatches(beaconIds, reference, chromosome, position, allele, batchSize);

        System.out.println("Retrieved beacon details: " + beaconDetails.size());

        return new BeaconSearchResults(organizationNames, beaconIds, beaconDetails);

    }

    @Override
    public List<BeaconDetail> getBeaconDetailsInBatches(List<String> beaconIds, String reference, String chromosome, String position, String allele, int batchSize) throws Exception {
        int batchCount = 0;
        List<String> beaconNamesCurrentBatch = new ArrayList<String>();
        List<CompletableFuture<List<BeaconDetail>>> tasks = new ArrayList<CompletableFuture<List<BeaconDetail>>>();

        System.out.println("Getting beacon details for: " + beaconIds.size());

        for (int i = 0; i < beaconIds.size(); i++) {
            beaconNamesCurrentBatch.add(beaconIds.get(i));
            batchCount++;

            //if we have reached 3 for the current batch, or last record
            if (batchCount == batchSize || i == beaconIds.size() - 1) {
                //create copy for thread
                List<String> beaconNamesForThread = new ArrayList<String>(beaconNamesCurrentBatch);

                tasks.add(beaconClientService.getBeaconDetails(reference, chromosome, position, allele, beaconNamesForThread));

                batchCount = 0;
                beaconNamesCurrentBatch.clear();
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
