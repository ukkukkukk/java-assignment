package com.dnastack.interview.beaconsummarizer;

import com.dnastack.interview.beaconsummarizer.client.beacon.Beacon;
import com.dnastack.interview.beaconsummarizer.client.beacon.BeaconClient;
import com.dnastack.interview.beaconsummarizer.client.beacon.BeaconDetail;
import com.dnastack.interview.beaconsummarizer.client.beacon.Organization;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

@Service
public class BeaconLookupService {

    @Autowired
    private BeaconClient beaconClient;

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<List<Organization>> getOrganizations() {
        System.out.println(Thread.currentThread().getName() + " getting organizations");
        List<Organization> organizations = null;

        try {
            organizations = beaconClient.getOrganizations()
                    .stream()
                    .collect(toList());
        } catch (FeignException exception) {
            System.out.println("Feign client exception occurred: " + exception.getMessage());
        }


        return CompletableFuture.completedFuture(organizations);
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<List<Beacon>> getBeacons() {
        System.out.println(Thread.currentThread().getName() + " getting beacons");
        List<Beacon> beacons = null;

        try {
            beacons = beaconClient.getBeacons()
                    .stream()
                    .collect(toList());
        } catch (FeignException exception) {
            System.out.println("Feign client exception occurred: " + exception.getMessage());
        }


        return CompletableFuture.completedFuture(beacons);
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<List<BeaconDetail>> getBeaconDetails(String ref, String chrom, String pos, String allele, List<String> beaconIds) {
        System.out.println(Thread.currentThread().getName() + " getting beacons details " + beaconIds);
        List<BeaconDetail> beaconDetails = null;

        try {
            beaconDetails = beaconClient.getBeaconDetails(chrom, pos, allele, ref, beaconIds.toArray(new String[beaconIds.size()]))
                    .stream()
                    .collect(toList());
        } catch (FeignException exception) {
            System.out.println("Feign client exception occurred: " + exception.getMessage());
        }


        return CompletableFuture.completedFuture(beaconDetails);
    }

}
