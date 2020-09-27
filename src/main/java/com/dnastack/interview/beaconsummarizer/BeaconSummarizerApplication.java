package com.dnastack.interview.beaconsummarizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableFeignClients
@EnableAsync

public class BeaconSummarizerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeaconSummarizerApplication.class, args);
    }

    @Bean("threadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors()); //when setting thread pool size here, hardware i.e. core count is not the only limitation, need to look at what the async methods are doing
        //in our case, we are calling another endpoint via HTTP, so need to see how many simultaneous connections this endpoint can support and don't want to introduce too much load in a short burst
        //i.e. if we have 82 beacons, don't want to make 82 calls all at once
        //setCorePoolSize = JVM will create a new thread for each new task until this limit is reached , remaining new tasks are put on a queue
        //once queue limit is reached, JVM will create a new thread for each new task until maxPoolSize is met
        //once maxPoolSize is met, new tasks will be rejected

        executor.setThreadNamePrefix("Async-");
        return executor;
    }


}

/*improvements:
1) move orchestration logic out of rest controller, and have intermediate service class
    -this will allow you to reuse that orchestration logic that is involved with a search, for other operations that may require it
    -business logic should go in service layer
2) instead of concrete class (BeaconService, BeaconClient?), use interfaces (allows the implementation to be more flexible in the future)
3) find ideal thread pool size for optimal performance
4) instead of passing copy of the list into each thread, I could have passed in start/end index for that batch
 */
