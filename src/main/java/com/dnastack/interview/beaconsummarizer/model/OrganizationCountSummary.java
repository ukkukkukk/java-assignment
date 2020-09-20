package com.dnastack.interview.beaconsummarizer.model;

public class OrganizationCountSummary {
    private int beaconsRespondedCount;
    private int beaconsFoundCount;
    private int beaconsNotFoundCount;
    private int beaconsNotApplicableCount;

    public int getBeaconsRespondedCount() {
        return beaconsRespondedCount;
    }

    public int getBeaconsFoundCount() {
        return beaconsFoundCount;
    }

    public int getBeaconsNotFoundCount() {
        return beaconsNotFoundCount;
    }

    public int getBeaconsNotApplicableCount() {
        return beaconsNotApplicableCount;
    }

    public void incrementBeaconsRespondedCount() {
        beaconsRespondedCount++;
    }

    public void incrementBeaconsFoundCount() {
        beaconsFoundCount++;
    }

    public void incrementBeaconsNotFoundCount() {
        beaconsNotFoundCount++;
    }

    public void incrementBeaconsNotApplicableCount() {
        beaconsNotApplicableCount++;
    }

}
