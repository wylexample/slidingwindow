package com.ylwu.slidingwindow.compare;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Auther: Wuyulong
 * @Date: 2019/3/10 20:15
 * @Description:
 */
public class LongAddrDemo {

    private static int threadCount = 500;
    private static ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    private static CompletionService<Long> service = new ExecutorCompletionService<>(executor);
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Instant start = Instant.now();
        LongAdder count = new LongAdder();
        int j = 0;
        while (j++<=threadCount){
            service.submit(() -> {
                int i = 0;
                while (i++ < 1000000){
                    count.increment();
                }
                return 1L;
            });
        }
        for (int i = 0; i < threadCount; i++) {
            Future<Long> future = service.take();
            future.get();
        }
        System.out.println("==="+Duration.between(start, Instant.now()).toMillis());
        executor.shutdown();
    }
}
