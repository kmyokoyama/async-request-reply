package io.kmyokoyama.asyncrequestreply.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class WorkExecutor {

    public void schedule(final Runnable r, final long delay, final TimeUnit timeUnit) {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        executorService.schedule(r, delay, timeUnit);
    }
}
