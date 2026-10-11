package com.javaguide.core.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 并发 - 线程池详解
 * <p>核心参数：corePoolSize、maximumPoolSize、keepAliveTime、workQueue、handler（拒绝策略）。
 * 拒绝策略：AbortPolicy(默认抛异常)/CallerRunsPolicy/DiscardPolicy/DiscardOldestPolicy。</p>
 */
public class ThreadPoolDemo {

    public static void main(String[] args) throws InterruptedException {
        AtomicInteger rejected = new AtomicInteger();
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                2,                 // 核心线程
                4,                 // 最大线程
                10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2), // 有界队列
                r -> new Thread(r, "demo-pool-" + rejected.getAndIncrement()),
                new ThreadPoolExecutor.AbortPolicy()
        );

        int total = 10;
        CountDownLatch latch = new CountDownLatch(total);
        for (int i = 0; i < total; i++) {
            try {
                pool.execute(() -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(50);
                    } catch (InterruptedException ignored) {
                    } finally {
                        latch.countDown();
                    }
                });
            } catch (Exception e) {
                System.out.println("第 " + i + " 个任务被拒绝: " + e.getClass().getSimpleName());
                latch.countDown();
            }
        }
        latch.await();
        System.out.println("完成。corePoolSize=" + pool.getCorePoolSize()
                + ", maxPoolSize=" + pool.getMaximumPoolSize()
                + ", queueCapacity=2（先占满队列再扩线程，仍满则拒绝）");
        pool.shutdown();
    }
}
