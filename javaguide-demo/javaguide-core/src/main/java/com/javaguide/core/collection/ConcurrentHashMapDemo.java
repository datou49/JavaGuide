package com.javaguide.core.collection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 集合 - ConcurrentHashMap 线程安全演示
 * <p>JDK8+ 采用 CAS + synchronized 锁桶头节点，size() 用 LongAdder 累加器。
 * 这里用多线程并发 put 验证不丢数据。</p>
 */
public class ConcurrentHashMapDemo {

    private static final int THREADS = 8;
    private static final int PER_THREAD = 10_000;

    public static void main(String[] args) throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        CountDownLatch latch = new CountDownLatch(THREADS);
        AtomicInteger failures = new AtomicInteger();

        for (int t = 0; t < THREADS; t++) {
            final int base = t;
            pool.submit(() -> {
                try {
                    for (int i = 0; i < PER_THREAD; i++) {
                        String key = "k-" + base + "-" + i;
                        map.put(key, i);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        pool.shutdown();

        int expected = THREADS * PER_THREAD;
        System.out.println("并发写入总量 = " + expected + "，ConcurrentHashMap.size = " + map.size());
        System.out.println("是否全部写入成功(无丢失) = " + (map.size() == expected));
    }
}
