package com.dnastack.interview.beaconsummarizer.service;

import com.dnastack.interview.beaconsummarizer.client.beacon.BeaconDetail;
import com.dnastack.interview.beaconsummarizer.model.BeaconSearchResults;

import java.util.List;

public interface IBeaconSearchService {
    BeaconSearchResults searchAllBeacons(String reference, String chromosome, String position, String allele, int batchSize) throws Exception;

    List<BeaconDetail> getBeaconDetailsInBatches(List<String> beaconIds, String reference, String chromosome, String position, String allele, int batchSize) throws Exception;
}
