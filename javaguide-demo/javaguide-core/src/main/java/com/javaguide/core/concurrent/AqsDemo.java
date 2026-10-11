package com.javaguide.core.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * 并发 - AQS（AbstractQueuedSynchronizer）框架演示
 * <p>ReentrantLock、Semaphore、CountDownLatch 都基于 AQS。AQS 维护一个 volatile state + CLH 双向队列，
 * 通过 CAS + 队列实现阻塞/唤醒。这里演示基于 AQS 的 CountDownLatch 与 Semaphore。</p>
 */
public class AqsDemo {

    public static void main(String[] args) throws InterruptedException {
        // CountDownLatch：主线程等所有子任务完成
        int tasks = 5;
        CountDownLatch latch = new CountDownLatch(tasks);
        for (int i = 0; i < tasks; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {
                }
                latch.countDown();
            }).start();
        }
        latch.await();
        System.out.println("CountDownLatch: " + tasks + " 个任务全部完成，主线程继续");

        // Semaphore：信号量控制并发数（限流）
        Semaphore sem = new Semaphore(2);
        long start = System.currentTimeMillis();
        CountDownLatch done = new CountDownLatch(6);
        for (int i = 0; i < 6; i++) {
            new Thread(() -> {
                try {
                    sem.acquire();
                    Thread.sleep(100);
                    done.countDown();
                } catch (InterruptedException ignored) {
                } finally {
                    sem.release();
                }
            }).start();
        }
        done.await();
        System.out.println("Semaphore(2) 处理 6 个任务耗时约 " + (System.currentTimeMillis() - start) + "ms（>400ms 说明只允许 2 并发）");
    }
}
