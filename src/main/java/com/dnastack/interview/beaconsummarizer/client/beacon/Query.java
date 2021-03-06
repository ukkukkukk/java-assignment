package com.dnastack.interview.beaconsummarizer.client.beacon;

import lombok.Data;

@Data
public class Query {
    private String chromosome;
    private Long position;
    private String allele;
    private String reference;
}
