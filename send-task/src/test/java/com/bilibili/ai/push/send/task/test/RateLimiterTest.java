package com.bilibili.ai.push.send.task.test;

import com.bilibili.ai.push.send.task.utils.RedisRateLimiter;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Bangkura
 */
public class RateLimiterTest {
    @Test
    public void test() throws Exception {
        final RedisRateLimiter redisRateLimiter = RedisRateLimiter.getInstance("test_lock", 7500, 4);
        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(3000);
        final AtomicInteger counter = new AtomicInteger(0);
        final AtomicInteger qpsCounter = new AtomicInteger(0);
        final long start = System.currentTimeMillis() / 1000;
        final AtomicLong timeStamp = new AtomicLong(System.currentTimeMillis() / 1000);
        for (int i = 0; i < 1000; i++) {
            threadPoolExecutor.submit(() -> {
                for (int j = 0; j < 1000; j++) {
                    try {
                        long startTime = System.currentTimeMillis();
                        redisRateLimiter.tryAcquire();
                        System.out.println("time elapse: " + (System.currentTimeMillis() - startTime));
                        counter.incrementAndGet();
                        qpsCounter.incrementAndGet();
                        timeStamp.set(System.currentTimeMillis() / 1000);
                        System.out.println("qps is: " + (qpsCounter.get() / (timeStamp.get() - start)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    counter.decrementAndGet();
                }
            });
        }
        threadPoolExecutor.awaitTermination(1000, TimeUnit.SECONDS);
    }
}
