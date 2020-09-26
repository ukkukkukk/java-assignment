package com.dnastack.interview.beaconsummarizer;

import com.dnastack.interview.beaconsummarizer.client.beacon.BeaconDetail;
import com.dnastack.interview.beaconsummarizer.model.BeaconSummary;
import com.dnastack.interview.beaconsummarizer.model.OrganizationCountSummary;
import com.dnastack.interview.beaconsummarizer.model.OrganizationSummary;

import java.util.*;
import java.util.stream.Collectors;

public class SummarizeResultsHelper {

    public static Map<String, OrganizationCountSummary> findBeaconCountsByOrganization(List<BeaconDetail> beaconDetails) {
        Map<String, OrganizationCountSummary> countsByOrganization = new Hashtable<String, OrganizationCountSummary>();

        for (BeaconDetail beaconDetail : beaconDetails) {
            OrganizationCountSummary currentCounts;
            String organizationName = beaconDetail.getBeacon().getOrganization();


            if (countsByOrganization.containsKey(organizationName))
                currentCounts = countsByOrganization.get(organizationName);
            else
                currentCounts = new OrganizationCountSummary();

            Boolean beaconResponse = beaconDetail.getResponse();

            if (beaconResponse == null)
                currentCounts.incrementBeaconsNotApplicableCount();
            else if (beaconResponse)
                currentCounts.incrementBeaconsFoundCount();
            else
                currentCounts.incrementBeaconsNotFoundCount();

            currentCounts.incrementBeaconsRespondedCount();

            countsByOrganization.put(organizationName, currentCounts);
        }

        return countsByOrganization;

    }

    public static BeaconSummary analyzeOrganizationCountSummary(List<BeaconDetail> beaconDetails, Map<String, OrganizationCountSummary> countsByOrganization, List<String> beaconIds, List<String> organizationNames) {

        //analyze counts to form response
        List<OrganizationSummary> organizationSummaries = new ArrayList<OrganizationSummary>();

        int foundBeaconsCount = 0;
        int notFoundBeaconsCount = 0;
        int notApplicableBeaconsCount = 0;
        int notRespondingBeaconsCount = beaconIds.size() - beaconDetails.size();

        Set<String> respondedBeaconOrganizations = countsByOrganization.keySet();

        for (String organizationName : respondedBeaconOrganizations) {
            OrganizationCountSummary organizationCountSummary = countsByOrganization.get(organizationName);
            foundBeaconsCount = foundBeaconsCount + organizationCountSummary.getBeaconsFoundCount();
            notFoundBeaconsCount = notFoundBeaconsCount + organizationCountSummary.getBeaconsNotFoundCount();
            notApplicableBeaconsCount = notApplicableBeaconsCount + organizationCountSummary.getBeaconsNotApplicableCount();

            int respondedCount = organizationCountSummary.getBeaconsRespondedCount();
            organizationSummaries.add(new OrganizationSummary(organizationName, respondedCount));
        }

        //sort by count, if tied, sort by alpha, in place sort
        organizationSummaries.sort((OrganizationSummary org1, OrganizationSummary org2) -> {
            if(org2.getBeacons() > org1.getBeacons())
                return 1;
            else if (org2.getBeacons() < org1.getBeacons())
                return -1;
            else
            {
                return org1.getOrganization().compareTo(org2.getOrganization());
            }
        });

        //this creates new list
        List<OrganizationSummary> sorted = organizationSummaries.stream().sorted(Comparator.comparing(OrganizationSummary::getBeacons).reversed().thenComparing(i -> i.getOrganization().toLowerCase())).collect(Collectors.toList());

        System.out.println(sorted);

        //add all the organizations that did not respond
        for (String organizationName : organizationNames) {
            if (!respondedBeaconOrganizations.contains(organizationName)) {
                organizationSummaries.add(new OrganizationSummary(organizationName, 0));
            }
        }

        return new BeaconSummary(organizationSummaries, foundBeaconsCount, notFoundBeaconsCount, notApplicableBeaconsCount, notRespondingBeaconsCount);
    }
}
