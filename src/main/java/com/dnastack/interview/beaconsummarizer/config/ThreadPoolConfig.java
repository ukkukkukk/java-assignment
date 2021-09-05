package com.dnastack.interview.beaconsummarizer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class ThreadPoolConfig {
    @Value("${app.thread-pool-size}")
    private Integer threadPoolSize;

    @Bean("threadPoolTaskExecutor")
    public Executor getAsyncExecutor() {
        return Executors.newFixedThreadPool(threadPoolSize);
    }
}
