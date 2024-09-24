package com.tracker.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author Raj Aryan
 * Created on 24/09/24.
 */
@Slf4j
@Configuration
@EnableAsync(proxyTargetClass = true)
public class AsyncConfiguration implements AsyncConfigurer {

    @Value("${async.executor.core.pool.size:5}")
    private int aeCorePoolSize;

    @Value("${async.executor.max.pool.size:10}")
    private int aeMaxPoolSize;

    @Override
    @Bean("asyncExecutor")
    public Executor getAsyncExecutor() {
        log.info("Creating Async Executor with core pool size: {} and max pool size: {}", aeCorePoolSize, aeMaxPoolSize);
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(aeCorePoolSize);
        executor.setMaxPoolSize(aeMaxPoolSize);
        executor.setThreadNamePrefix("AsyncThread-");
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (e, method, params) ->
                log.error("An unknown error occurred while executing tasks in the thread pool. Executing method: {} params {}", method.getName(), params, e);
    }
}
