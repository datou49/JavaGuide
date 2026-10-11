package com.javaguide.core.concurrent;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 并发 - 虚拟线程（JDK 21 特性，JavaGuide 新特性部分）
 * <p>虚拟线程（Virtual Thread）是轻量级线程，适合大量 IO 阻塞任务，可显著降低线程开销。</p>
 */
public class VirtualThreadDemo {

    public static void main(String[] args) throws Exception {
        int tasks = 2000;
        List<Callable<Integer>> work = new ArrayList<>();
        for (int i = 0; i < tasks; i++) {
            work.add(() -> {
                Thread.sleep(20); // 模拟 IO 阻塞
                return 1;
            });
        }

        // 虚拟线程池
        try (ExecutorService vpool = Executors.newVirtualThreadPerTaskExecutor()) {
            Instant start = Instant.now();
            List<Future<Integer>> futures = vpool.invokeAll(work);
            int sum = futures.stream().mapToInt(f -> {
                try {
                    return f.get();
                } catch (Exception e) {
                    return 0;
                }
            }).sum();
            System.out.println("虚拟线程处理 " + tasks + " 个阻塞任务: sum=" + sum
                    + ", 耗时≈" + Duration.between(start, Instant.now()).toMillis() + "ms");
        }
    }
}
