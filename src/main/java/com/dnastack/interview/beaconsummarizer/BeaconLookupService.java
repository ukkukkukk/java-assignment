package com.dnastack.interview.beaconsummarizer;

import com.dnastack.interview.beaconsummarizer.client.beacon.Beacon;
import com.dnastack.interview.beaconsummarizer.client.beacon.BeaconClient;
import com.dnastack.interview.beaconsummarizer.client.beacon.Organization;
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
    public CompletableFuture<List<String>> getOrganizations() {
        System.out.println(Thread.currentThread().getName() + " getting organizations");
        List<String> orgNames = beaconClient.getOrganizations()
                .stream()
                .map(Organization::getName)
                .collect(toList());

        return CompletableFuture.completedFuture(orgNames);
    }

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<List<String>> getBeacons() {
        System.out.println(Thread.currentThread().getName() + " getting beacons");
        List<String> beaconNames = beaconClient.getBeacons()
                .stream()
                .map(Beacon::getName)
                .collect(toList());

        return CompletableFuture.completedFuture(beaconNames);
    }

}
