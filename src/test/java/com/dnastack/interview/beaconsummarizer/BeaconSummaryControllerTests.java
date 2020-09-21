package com.dnastack.interview.beaconsummarizer;

import com.dnastack.interview.beaconsummarizer.client.beacon.Beacon;
import com.dnastack.interview.beaconsummarizer.client.beacon.BeaconClient;
import com.dnastack.interview.beaconsummarizer.client.beacon.Organization;
import com.dnastack.interview.beaconsummarizer.model.BeaconSummary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeaconSummaryControllerTests {

    @Autowired
    private BeaconSummaryController beaconSummaryController;

    @Autowired
    private BeaconLookupService beaconLookupService;

    @Test
    public void testSearchControllerCorrectCount() throws Exception {

        BeaconSummary results = beaconSummaryController.search("GRCh37", "17", "41244981", "G", "G");
        int totalBeacons = results.getFound() + results.getNotApplicable() + results.getNotFound() + results.getNotResponding();

        CompletableFuture<List<Organization>> organizationResults = beaconLookupService.getOrganizations();
        CompletableFuture<List<Beacon>> beaconResults = beaconLookupService.getBeacons();

        CompletableFuture.allOf(organizationResults, beaconResults).join();

        Assert.isTrue(totalBeacons == beaconResults.get().size(), "total beacon count is incorrect");
        Assert.isTrue(results.getOrganizations().size() == organizationResults.get().size(), "organizations are missing");


    }

}
