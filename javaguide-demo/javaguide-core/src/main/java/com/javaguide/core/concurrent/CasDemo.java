package com.javaguide.core.concurrent;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 并发 - CAS 与 Atomic 原子类
 * <p>CAS（Compare And Swap）：比较并交换，无锁实现线程安全；ABA 问题可用 AtomicStampedReference 解决。
 * AtomicInteger 底层通过 Unsafe.compareAndSwapInt 实现。</p>
 */
public class CasDemo {

    public static void main(String[] args) throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);

        int threads = 8;
        Thread[] ts = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            ts[i] = new Thread(() -> {
                for (int j = 0; j < 10_000; j++) {
                    counter.incrementAndGet(); // CAS 自旋
                }
            });
            ts[i].start();
        }
        for (Thread t : ts) {
            t.join();
        }
        System.out.println("CAS 无锁累加 8*10000 => " + counter.get() + "（期望 80000）");

        AtomicReference<String> ref = new AtomicReference<>("A");
        System.out.println("CAS 更新 A->B 成功? " + ref.compareAndSet("A", "B")
                + "，当前=" + ref.get() + "（重新设回 A 即 ABA 场景，需版本号解决）");
    }
}
