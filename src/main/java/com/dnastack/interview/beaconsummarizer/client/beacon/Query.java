package com.dnastack.interview.beaconsummarizer.client.beacon;

import lombok.Data;

@Data
public class Query {
    private String chromosome;
    private long position;
    private String allele;
    private String reference;
}
