package com.dnastack.interview.beaconsummarizer.service;

import com.dnastack.interview.beaconsummarizer.client.beacon.Beacon;
import com.dnastack.interview.beaconsummarizer.client.beacon.BeaconClient;
import com.dnastack.interview.beaconsummarizer.client.beacon.BeaconDetail;
import com.dnastack.interview.beaconsummarizer.client.beacon.Organization;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static java.util.stream.Collectors.toList;

@Service("beaconLookUpService")
public class BeaconService implements IBeaconService{

    private static int BATCH_SIZE = 3;

    @Autowired
    private BeaconClient beaconClient;

    @Async("threadPoolTaskExecutor")
    @Override
    public CompletableFuture<List<Organization>> getOrganizations() {
        System.out.println(Thread.currentThread().getName() + " getting organizations");
        List<Organization> organizations = null;

        try {
            organizations = beaconClient.getOrganizations();
        } catch (FeignException exception) {
            System.out.println("Feign client exception occurred: " + exception.getMessage());
        }


        return CompletableFuture.completedFuture(organizations);
    }

    @Async("threadPoolTaskExecutor")
    @Override
    public CompletableFuture<List<Beacon>> getBeacons() {
        System.out.println(Thread.currentThread().getName() + " getting beacons");
        List<Beacon> beacons = null;

        try {
            beacons = beaconClient.getBeacons();
        } catch (FeignException exception) {
            System.out.println("Feign client exception occurred: " + exception.getMessage());
        }


        return CompletableFuture.completedFuture(beacons);
    }

    @Async("threadPoolTaskExecutor")
    @Override
    public CompletableFuture<List<BeaconDetail>> getBeaconDetails(String reference, String chromosome, String position, String allele, List<String> beaconIds) {
        System.out.println(Thread.currentThread().getName() + " getting beacons details " + beaconIds);
        List<BeaconDetail> beaconDetails = null;


        try {
            beaconDetails = beaconClient.getBeaconDetails(chromosome, position, allele, reference, createBeaconIdsQueryParam(beaconIds));
        } catch (FeignException exception) {
            System.out.println("Feign client exception occurred: " + exception.getMessage());
        }


        return CompletableFuture.completedFuture(beaconDetails);
    }

    private String createBeaconIdsQueryParam(List<String> beaconIds) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < beaconIds.size(); i++) {
            sb.append(beaconIds.get(i));

            if (i != beaconIds.size() - 1)
                sb.append(",");
        }

        sb.append("]");

        return sb.toString();
    }

    @Async("threadPoolTaskExecutor")
    @Override
    public CompletableFuture<List<BeaconDetail>> getBeaconDetailsInBatches(List<String> beaconIds, String reference, String chromosome, String position, String allele) throws Exception {
        int batchCount = 0;
        List<String> beaconNamesCurrentBatch = new ArrayList<String>();
        List<CompletableFuture<List<BeaconDetail>>> tasks = new ArrayList<CompletableFuture<List<BeaconDetail>>>();

        System.out.println("Getting beacon details for: " + beaconIds.size());

        for (int i = 0; i < beaconIds.size(); i++) {
            beaconNamesCurrentBatch.add(beaconIds.get(i));
            batchCount++;

            //if we have reached 3 for the current batch, or last record
            if (batchCount == BATCH_SIZE || i == beaconIds.size() - 1) {
                //create copy for thread
                List<String> beaconNamesForThread = new ArrayList<String>(beaconNamesCurrentBatch);

                //lambda expression can use variables from outside that are not parameters to the lambda expression
                // these are known as free variables
                //the lambda expression will store the values for these free variables at the time of creation (we can treat a lambda  expression as an object)
                //since we create new obj for each call, the mem address of this obj is copied to the lambda expression
                //the lambda expression can execute at a later time, but it will still reference the same obj
                //this is why START and END print the same list
                //https://www.informit.com/articles/article.aspx?p=2303960&seqNum=7

                //supplyAsync returns completable future that returns some value -> use lambda exp to implement T get()
                //runAsync returns completable future that returns no value -> use lambda exp to implement void run()
                //in both cases they take in a functional interface, so a lambda expression can be used

                tasks.add(CompletableFuture.supplyAsync( () -> {
                    System.out.println("START" + Thread.currentThread().getName() + " " + beaconNamesForThread);
                    List<BeaconDetail> beaconDetails = null;
                    try {
                        //even though getBeaconDetails is Async, this will be sync call in our new thread (since we are in the same class)
                        beaconDetails = getBeaconDetails(reference, chromosome, position, allele, beaconNamesForThread).get();
                    }
                    catch (Exception e)
                    {
                        System.out.println("Error occurred. ");
                        throw new CompletionException(e); //this will cause you to complete with error, when you do .get/.join , the exception is thrown
                    }
                    System.out.println("END " + Thread.currentThread().getName() + " " + beaconNamesForThread);
                    return beaconDetails;

                }));

                batchCount = 0;
                beaconNamesCurrentBatch.clear();
            }
        }

        //wait for above async calls to complete
        CompletableFuture.allOf(tasks.toArray(new CompletableFuture[tasks.size()])).join();

        List<BeaconDetail> beaconDetails = new ArrayList<BeaconDetail>();
        //append to final list
        for (CompletableFuture<List<BeaconDetail>> task : tasks) {
            List<BeaconDetail> beaconDetailResults = task.get();

            if (beaconDetailResults != null)
                beaconDetails.addAll(beaconDetailResults);
        }

        return CompletableFuture.completedFuture(beaconDetails);

    }

}
