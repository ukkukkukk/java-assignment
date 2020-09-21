package com.dnastack.interview.beaconsummarizer.client.beacon;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// This client uses Spring's wrapper of the Feign declarative HTTP client library.
// Instructions are at <https://cloud.spring.io/spring-cloud-netflix/multi/multi_spring-cloud-feign.html>

// Documentation for the Beacon REST API is at <https://beacon-network.org/#/developers/api/beacon-network>

@FeignClient("beacon")
public interface BeaconClient {

    @GetMapping("/api/organizations")
    List<Organization> getOrganizations();

    @GetMapping("/api/beacons")
    List<Beacon> getBeacons();

    @GetMapping("/api/responses")
    List<BeaconDetail> getBeaconDetails(@RequestParam(value = "chrom") String chromosome,
                                        @RequestParam(value = "pos") String position,
                                        @RequestParam(value = "allele") String allele,
                                        @RequestParam(value = "ref", required = false) String reference,
                                        @RequestParam(value = "beacon", required = false) String beacon);

}
