package com.dnastack.interview.beaconsummarizer.service;

import com.dnastack.interview.beaconsummarizer.client.beacon.Beacon;
import com.dnastack.interview.beaconsummarizer.client.beacon.BeaconDetail;
import com.dnastack.interview.beaconsummarizer.client.beacon.Organization;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IBeaconService {

    public CompletableFuture<List<Organization>> getOrganizations();
    public CompletableFuture<List<Beacon>> getBeacons();
    public CompletableFuture<List<BeaconDetail>> getBeaconDetails(String reference, String chromosome, String position, String allele, List<String> beaconIds);
}
