package com.dnastack.interview.beaconsummarizer.service;

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
public class BeaconClientService implements IBeaconClientService {

    @Autowired
    private BeaconClient beaconClient;

    @Async("threadPoolTaskExecutor")
    @Override
    public CompletableFuture<List<Organization>> getOrganizations() {
        System.out.println(Thread.currentThread().getName() + " getting organizations");
        List<Organization> organizations = null;

        try {
            organizations = beaconClient.getOrganizations();
        } catch (FeignException exception) {
            System.out.println("Feign client exception occurred: " + exception.getMessage());
        }


        return CompletableFuture.completedFuture(organizations);
    }

    @Async("threadPoolTaskExecutor")
    @Override
    public CompletableFuture<List<Beacon>> getBeacons() {
        System.out.println(Thread.currentThread().getName() + " getting beacons");
        List<Beacon> beacons = null;

        try {
            beacons = beaconClient.getBeacons();
        } catch (FeignException exception) {
            System.out.println("Feign client exception occurred: " + exception.getMessage());
        }


        return CompletableFuture.completedFuture(beacons);
    }

    @Async("threadPoolTaskExecutor")
    @Override
    public CompletableFuture<List<BeaconDetail>> getBeaconDetails(String reference, String chromosome, String position, String allele, List<String> beaconIds) {
        System.out.println(Thread.currentThread().getName() + " getting beacons details " + beaconIds);
        List<BeaconDetail> beaconDetails = null;


        try {
            beaconDetails = beaconClient.getBeaconDetails(chromosome, position, allele, reference, createBeaconIdsQueryParam(beaconIds));
        } catch (FeignException exception) {
            System.out.println("Feign client exception occurred: " + exception.getMessage());
        }


        return CompletableFuture.completedFuture(beaconDetails);
    }

    private String createBeaconIdsQueryParam(List<String> beaconIds) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < beaconIds.size(); i++) {
            sb.append(beaconIds.get(i));

            if (i != beaconIds.size() - 1)
                sb.append(",");
        }

        sb.append("]");

        return sb.toString();
    }

}
