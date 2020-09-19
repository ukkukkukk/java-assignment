package com.dnastack.interview.beaconsummarizer;

import com.dnastack.interview.beaconsummarizer.client.beacon.Beacon;
import com.dnastack.interview.beaconsummarizer.client.beacon.BeaconClient;
import com.dnastack.interview.beaconsummarizer.client.beacon.Organization;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;

public class BeaconLookupService {
    private BeaconClient beaconClient;

    public BeaconLookupService(BeaconClient beaconClient) {
        this.beaconClient = beaconClient;
    }

    @Async
    public CompletableFuture<List<String>> getOrganizations() {

        List<String> orgNames = beaconClient.getOrganizations()
                .stream()
                .map(Organization::getName)
                .collect(toList());

        return CompletableFuture.completedFuture(orgNames);
    }

    @Async
    public CompletableFuture<List<String>> getBeacons() throws Exception {

        List<String> beaconNames = beaconClient.getBeacons()
                .stream()
                .map(Beacon::getName)
                .collect(toList());

        return CompletableFuture.completedFuture(beaconNames);
    }

}
