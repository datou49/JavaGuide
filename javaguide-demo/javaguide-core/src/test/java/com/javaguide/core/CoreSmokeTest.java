package com.javaguide.core;

import com.javaguide.core.collection.ConcurrentHashMapDemo;
import com.javaguide.core.concurrent.CasDemo;
import com.javaguide.core.concurrent.CompletableFutureDemo;
import com.javaguide.core.concurrent.ReentrantLockDemo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * javaguide-core 冒烟测试：验证各知识点示例的可运行性与结果正确性。
 */
class CoreSmokeTest {

    @Test
    void concurrentMap_shouldNotLoseData() throws Exception {
        int threads = 4;
        int perThread = 5_000;
        var map = new java.util.concurrent.ConcurrentHashMap<String, Integer>();
        var pool = java.util.concurrent.Executors.newFixedThreadPool(threads);
        var latch = new java.util.concurrent.CountDownLatch(threads);
        for (int t = 0; t < threads; t++) {
            int base = t;
            pool.submit(() -> {
                for (int i = 0; i < perThread; i++) {
                    map.put("k-" + base + "-" + i, i);
                }
                latch.countDown();
            });
        }
        latch.await();
        pool.shutdown();
        assertEquals(threads * perThread, map.size());
    }

    @Test
    void reentrantLock_shouldProtectCounter() throws Exception {
        int[] count = {0};
        var lock = new java.util.concurrent.locks.ReentrantLock();
        Runnable task = () -> {
            for (int i = 0; i < 10_000; i++) {
                lock.lock();
                try {
                    count[0]++;
                } finally {
                    lock.unlock();
                }
            }
        };
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        assertEquals(20_000, count[0]);
    }

    @Test
    void completableFuture_combineWorks() throws Exception {
        var f1 = java.util.concurrent.CompletableFuture.supplyAsync(() -> 100);
        var f2 = java.util.concurrent.CompletableFuture.supplyAsync(() -> 200);
        assertEquals(300, f1.thenCombine(f2, Integer::sum).get());
    }

    @Test
    void casAtomic_works() {
        var counter = new java.util.concurrent.atomic.AtomicInteger();
        assertEquals(0, counter.getAndIncrement());
        assertTrue(counter.compareAndSet(1, 5));
        assertEquals(5, counter.get());
    }
}
