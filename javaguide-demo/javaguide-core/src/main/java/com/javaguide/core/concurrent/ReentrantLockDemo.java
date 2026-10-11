package com.javaguide.core.concurrent;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 并发 - Java 锁详解（ReentrantLock）
 * <p>与 synchronized 相比：可中断、可超时、可公平、可绑定多条件（Condition）。
 * 可重入：同一线程可重复获取同一把锁。</p>
 */
public class ReentrantLockDemo {

    private static int count = 0;
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            for (int i = 0; i < 10_000; i++) {
                lock.lock();
                try {
                    count++;
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

        System.out.println("ReentrantLock 保护后 count = " + count + "（期望 20000）");
        System.out.println("是否公平锁(默认false)=" + lock.isFair() + "，可重入示例：" + lock.getHoldCount());
    }
}
