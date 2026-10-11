package com.javaguide.core.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 并发 - ThreadLocal 详解（JavaGuide 必看）
 * <p>ThreadLocal 为每个线程保存独立副本，实现线程隔离。典型应用：购物车用户身份、Spring 事务连接、
 * SimpleDateFormat 线程安全。使用后记得 remove() 防止内存泄漏。</p>
 */
public class ThreadLocalDemo {

    private static final ThreadLocal<Integer> userContext = ThreadLocal.withInitial(() -> -1);

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        for (int i = 1; i <= 3; i++) {
            int uid = i;
            pool.submit(() -> {
                userContext.set(uid);
                System.out.println(Thread.currentThread().getName()
                        + " 读到自己的用户ID = " + userContext.get());
                userContext.remove();
            });
        }
        pool.shutdown();
    }
}
