package com.dnastack.interview.beaconsummarizer;

import com.dnastack.interview.beaconsummarizer.client.beacon.Beacon;
import com.dnastack.interview.beaconsummarizer.client.beacon.BeaconDetail;
import com.dnastack.interview.beaconsummarizer.client.beacon.Organization;
import com.dnastack.interview.beaconsummarizer.model.BeaconSummary;
import com.dnastack.interview.beaconsummarizer.model.OrganizationCountSummary;
import com.dnastack.interview.beaconsummarizer.model.OrganizationSummary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SummarizeResultsHelperTests {

    @Test
    public void testSummarizeResultsHelperCorrectCounts() {

        List<BeaconDetail> beaconDetails = new ArrayList<BeaconDetail>();

        Beacon testBeacon1 = new Beacon();
        testBeacon1.setOrganization("organization name 1");

        BeaconDetail testBeaconDetail1 = new BeaconDetail();
        testBeaconDetail1.setResponse(true);
        testBeaconDetail1.setBeacon(testBeacon1);

        Beacon testBeacon2 = new Beacon();
        testBeacon2.setOrganization("organization name 2");

        BeaconDetail testBeaconDetail2 = new BeaconDetail();
        testBeaconDetail2.setResponse(false);
        testBeaconDetail2.setBeacon(testBeacon2);

        Beacon testBeacon3 = new Beacon();
        testBeacon3.setOrganization("organization name 3");

        BeaconDetail testBeaconDetail3 = new BeaconDetail();
        testBeaconDetail3.setResponse(null);
        testBeaconDetail3.setBeacon(testBeacon3);

        Beacon testBeacon4 = new Beacon();
        testBeacon4.setOrganization("organization name 1");

        BeaconDetail testBeaconDetail4 = new BeaconDetail();
        testBeaconDetail4.setResponse(null);
        testBeaconDetail4.setBeacon(testBeacon4);

        beaconDetails.add(testBeaconDetail1);
        beaconDetails.add(testBeaconDetail2);
        beaconDetails.add(testBeaconDetail3);
        beaconDetails.add(testBeaconDetail4);

        Map<String, OrganizationCountSummary> organizationCounts = SummarizeResultsHelper.findBeaconCountsByOrganization(beaconDetails);

        Assert.isTrue(organizationCounts.keySet().size() == 3, "incorrect number of organizations");

        OrganizationCountSummary organizationCountSummary1 = organizationCounts.get("organization name 1");

        Assert.isTrue(organizationCountSummary1.getBeaconsFoundCount() == 1 && organizationCountSummary1.getBeaconsNotApplicableCount() == 1 && organizationCountSummary1.getBeaconsNotFoundCount() == 0 && organizationCountSummary1.getBeaconsRespondedCount() == 2, "incorrect counts");

        OrganizationCountSummary organizationCountSummary2 = organizationCounts.get("organization name 2");

        Assert.isTrue(organizationCountSummary2.getBeaconsFoundCount() == 0 && organizationCountSummary2.getBeaconsNotApplicableCount() == 0 && organizationCountSummary2.getBeaconsNotFoundCount() == 1 && organizationCountSummary2.getBeaconsRespondedCount() == 1, "incorrect counts");

        OrganizationCountSummary organizationCountSummary3 = organizationCounts.get("organization name 3");

        Assert.isTrue(organizationCountSummary3.getBeaconsFoundCount() == 0 && organizationCountSummary3.getBeaconsNotApplicableCount() == 1 && organizationCountSummary3.getBeaconsNotFoundCount() == 0 && organizationCountSummary3.getBeaconsRespondedCount() == 1, "incorrect counts");

    }


    @Test
    public void testSummarizeResultsHelperCorrectSummary() {

        List<BeaconDetail> beaconDetails = new ArrayList<BeaconDetail>();

        Beacon testBeacon1 = new Beacon();
        testBeacon1.setOrganization("organization name 1");

        BeaconDetail testBeaconDetail1 = new BeaconDetail();
        testBeaconDetail1.setResponse(true);
        testBeaconDetail1.setBeacon(testBeacon1);

        Beacon testBeacon2 = new Beacon();
        testBeacon2.setOrganization("organization name 2");

        BeaconDetail testBeaconDetail2 = new BeaconDetail();
        testBeaconDetail2.setResponse(false);
        testBeaconDetail2.setBeacon(testBeacon2);


        Beacon testBeacon4 = new Beacon();
        testBeacon4.setOrganization("organization name 1");

        BeaconDetail testBeaconDetail4 = new BeaconDetail();
        testBeaconDetail4.setResponse(null);
        testBeaconDetail4.setBeacon(testBeacon4);

        beaconDetails.add(testBeaconDetail1);
        beaconDetails.add(testBeaconDetail2);
        beaconDetails.add(testBeaconDetail4);

        Map<String, OrganizationCountSummary> organizationCounts = SummarizeResultsHelper.findBeaconCountsByOrganization(beaconDetails);

        List<String> organizationNames = new ArrayList<String>();
        organizationNames.add("organization name 1");
        organizationNames.add("organization name 2");
        organizationNames.add("organization name 3");

        List<String> beaconIds = new ArrayList<String>();
        beaconIds.add("testBeacon1");
        beaconIds.add("testBeacon2");
        beaconIds.add("testBeacon3");
        beaconIds.add("testBeacon4");

        BeaconSummary beaconSummary = SummarizeResultsHelper.analyzeOrganizationCountSummary(beaconDetails, organizationCounts, beaconIds, organizationNames);

        Assert.isTrue(beaconSummary.getOrganizations().size() == organizationNames.size(), "incorrect count");

        OrganizationSummary organization1 = beaconSummary.getOrganizations().get(0);

        Assert.isTrue(organization1.getOrganization().equals("organization name 1") && organization1.getBeacons() == 2, "incorrect count");

        OrganizationSummary organization2 = beaconSummary.getOrganizations().get(1);

        Assert.isTrue(organization2.getOrganization().equals("organization name 2") && organization2.getBeacons() == 1, "incorrect count");

        OrganizationSummary organization3 = beaconSummary.getOrganizations().get(2);

        Assert.isTrue(organization3.getOrganization().equals("organization name 3") && organization3.getBeacons() == 0, "incorrect count");

        Assert.isTrue(beaconSummary.getFound() == 1, "incorrect count");
        Assert.isTrue(beaconSummary.getNotFound() == 1, "incorrect count");
        Assert.isTrue(beaconSummary.getNotApplicable() == 1, "incorrect count");
        Assert.isTrue(beaconSummary.getNotResponding() == 1, "incorrect count");

    }

    @Test
    public void testSummarizeResultsHelperCorrectSummaryFoundOnly() {

        List<BeaconDetail> beaconDetails = new ArrayList<BeaconDetail>();

        Beacon testBeacon1 = new Beacon();
        testBeacon1.setOrganization("organization name 1");

        BeaconDetail testBeaconDetail1 = new BeaconDetail();
        testBeaconDetail1.setResponse(true);
        testBeaconDetail1.setBeacon(testBeacon1);


        beaconDetails.add(testBeaconDetail1);


        Map<String, OrganizationCountSummary> organizationCounts = SummarizeResultsHelper.findBeaconCountsByOrganization(beaconDetails);

        List<String> organizationNames = new ArrayList<String>();
        organizationNames.add("organization name 1");
        organizationNames.add("organization name 2");
        organizationNames.add("organization name 3");

        List<String> beaconIds = new ArrayList<String>();
        beaconIds.add("testBeacon1");
        beaconIds.add("testBeacon2");
        beaconIds.add("testBeacon3");
        beaconIds.add("testBeacon4");

        BeaconSummary beaconSummary = SummarizeResultsHelper.analyzeOrganizationCountSummary(beaconDetails, organizationCounts, beaconIds, organizationNames);

        Assert.isTrue(beaconSummary.getOrganizations().size() == organizationNames.size(), "incorrect count");

        OrganizationSummary organization1 = beaconSummary.getOrganizations().get(0);

        Assert.isTrue(organization1.getOrganization().equals("organization name 1") && organization1.getBeacons() == 1, "incorrect count");


        Assert.isTrue(beaconSummary.getFound() == 1, "incorrect count");
        Assert.isTrue(beaconSummary.getNotFound() == 0, "incorrect count");
        Assert.isTrue(beaconSummary.getNotApplicable() == 0, "incorrect count");
        Assert.isTrue(beaconSummary.getNotResponding() == 3, "incorrect count");

    }

    @Test
    public void testSummarizeResultsHelperCorrectSummaryNotFoundOnly() {

        List<BeaconDetail> beaconDetails = new ArrayList<BeaconDetail>();

        Beacon testBeacon1 = new Beacon();
        testBeacon1.setOrganization("organization name 1");

        BeaconDetail testBeaconDetail1 = new BeaconDetail();
        testBeaconDetail1.setResponse(false);
        testBeaconDetail1.setBeacon(testBeacon1);


        beaconDetails.add(testBeaconDetail1);


        Map<String, OrganizationCountSummary> organizationCounts = SummarizeResultsHelper.findBeaconCountsByOrganization(beaconDetails);

        List<String> organizationNames = new ArrayList<String>();
        organizationNames.add("organization name 1");
        organizationNames.add("organization name 2");
        organizationNames.add("organization name 3");

        List<String> beaconIds = new ArrayList<String>();
        beaconIds.add("testBeacon1");
        beaconIds.add("testBeacon2");
        beaconIds.add("testBeacon3");
        beaconIds.add("testBeacon4");

        BeaconSummary beaconSummary = SummarizeResultsHelper.analyzeOrganizationCountSummary(beaconDetails, organizationCounts, beaconIds, organizationNames);

        Assert.isTrue(beaconSummary.getOrganizations().size() == organizationNames.size(), "incorrect count");

        OrganizationSummary organization1 = beaconSummary.getOrganizations().get(0);

        Assert.isTrue(organization1.getOrganization().equals("organization name 1") && organization1.getBeacons() == 1, "incorrect count");


        Assert.isTrue(beaconSummary.getFound() == 0, "incorrect count");
        Assert.isTrue(beaconSummary.getNotFound() == 1, "incorrect count");
        Assert.isTrue(beaconSummary.getNotApplicable() == 0, "incorrect count");
        Assert.isTrue(beaconSummary.getNotResponding() == 3, "incorrect count");

    }

    @Test
    public void testSummarizeResultsHelperCorrectSummaryNotApplicableOnly() {

        List<BeaconDetail> beaconDetails = new ArrayList<BeaconDetail>();

        Beacon testBeacon1 = new Beacon();
        testBeacon1.setOrganization("organization name 1");

        BeaconDetail testBeaconDetail1 = new BeaconDetail();
        testBeaconDetail1.setResponse(null);
        testBeaconDetail1.setBeacon(testBeacon1);


        beaconDetails.add(testBeaconDetail1);


        Map<String, OrganizationCountSummary> organizationCounts = SummarizeResultsHelper.findBeaconCountsByOrganization(beaconDetails);

        List<String> organizationNames = new ArrayList<String>();
        organizationNames.add("organization name 1");
        organizationNames.add("organization name 2");
        organizationNames.add("organization name 3");

        List<String> beaconIds = new ArrayList<String>();
        beaconIds.add("testBeacon1");
        beaconIds.add("testBeacon2");
        beaconIds.add("testBeacon3");
        beaconIds.add("testBeacon4");

        BeaconSummary beaconSummary = SummarizeResultsHelper.analyzeOrganizationCountSummary(beaconDetails, organizationCounts, beaconIds, organizationNames);

        Assert.isTrue(beaconSummary.getOrganizations().size() == organizationNames.size(), "incorrect count");

        OrganizationSummary organization1 = beaconSummary.getOrganizations().get(0);

        Assert.isTrue(organization1.getOrganization().equals("organization name 1") && organization1.getBeacons() == 1, "incorrect count");


        Assert.isTrue(beaconSummary.getFound() == 0, "incorrect count");
        Assert.isTrue(beaconSummary.getNotFound() == 0, "incorrect count");
        Assert.isTrue(beaconSummary.getNotApplicable() == 1, "incorrect count");
        Assert.isTrue(beaconSummary.getNotResponding() == 3, "incorrect count");

    }

    @Test
    public void testSummarizeResultsHelperCorrectSummaryNoResponses() {

        List<BeaconDetail> beaconDetails = new ArrayList<BeaconDetail>();


        Map<String, OrganizationCountSummary> organizationCounts = SummarizeResultsHelper.findBeaconCountsByOrganization(beaconDetails);

        List<String> organizationNames = new ArrayList<String>();
        organizationNames.add("organization name 1");
        organizationNames.add("organization name 2");
        organizationNames.add("organization name 3");

        List<String> beaconIds = new ArrayList<String>();
        beaconIds.add("testBeacon1");
        beaconIds.add("testBeacon2");
        beaconIds.add("testBeacon3");
        beaconIds.add("testBeacon4");

        BeaconSummary beaconSummary = SummarizeResultsHelper.analyzeOrganizationCountSummary(beaconDetails, organizationCounts, beaconIds, organizationNames);

        Assert.isTrue(beaconSummary.getOrganizations().size() == organizationNames.size(), "incorrect count");

        for (OrganizationSummary organizationSummary : beaconSummary.getOrganizations())
            Assert.isTrue(organizationSummary.getBeacons() == 0, "incorrect count");


        Assert.isTrue(beaconSummary.getFound() == 0, "incorrect count");
        Assert.isTrue(beaconSummary.getNotFound() == 0, "incorrect count");
        Assert.isTrue(beaconSummary.getNotApplicable() == 0, "incorrect count");
        Assert.isTrue(beaconSummary.getNotResponding() == 4, "incorrect count");

    }

}
