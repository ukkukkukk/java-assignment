package com.dnastack.interview.beaconsummarizer;

import com.dnastack.interview.beaconsummarizer.client.beacon.Beacon;
import com.dnastack.interview.beaconsummarizer.client.beacon.Organization;
import com.dnastack.interview.beaconsummarizer.model.BeaconSummary;
import com.dnastack.interview.beaconsummarizer.service.IBeaconClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BeaconSummaryControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private IBeaconClientService beaconClientService;

    @Test
    public void testSearchControllerCorrectCount() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/search")
                .param("ref", "GRCh37")
                .param("chrom", "17")
                .param("pos", "41244981")
                .param("allele", "G")
                .param("referenceAllele", "A")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        Assert.isTrue(response.getStatus() == 200, "error response from endpoint");

        ObjectMapper mapper = new ObjectMapper();

        BeaconSummary results = mapper.readValue(response.getContentAsString(), BeaconSummary.class);
        int totalBeacons = results.getFound() + results.getNotApplicable() + results.getNotFound() + results.getNotResponding();

        CompletableFuture<List<Organization>> organizationResults = beaconClientService.getOrganizations();
        CompletableFuture<List<Beacon>> beaconResults = beaconClientService.getBeacons();

        CompletableFuture.allOf(organizationResults, beaconResults).join();

        Assert.isTrue(totalBeacons == beaconResults.get().size(), "total beacon count is incorrect");
        Assert.isTrue(results.getOrganizations().size() == organizationResults.get().size(), "organizations are missing");
    }
}
