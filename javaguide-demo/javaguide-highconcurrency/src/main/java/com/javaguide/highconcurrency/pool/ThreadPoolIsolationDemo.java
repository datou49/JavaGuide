package com.javaguide.highconcurrency.pool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池隔离（舱壁模式）演示
 * <p>把不同依赖的调用隔离到不同线程池，避免某个慢依赖占满所有线程导致整个应用雪崩。
 * 例：下单流程中「查库存」「查价格」「查优惠」各自独立的线程池。</p>
 */
public class ThreadPoolIsolationDemo {

    static class Service {
        final String name;
        final ThreadPoolExecutor pool;

        Service(String name, int threads) {
            this.name = name;
            this.pool = new ThreadPoolExecutor(threads, threads, 0, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(10));
        }

        void submit(Runnable task) {
            pool.execute(() -> {
                try {
                    task.run();
                } catch (Exception e) {
                    System.out.println(name + " 线程池任务异常: " + e.getMessage());
                }
            });
        }

        int active() {
            return pool.getActiveCount();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== 线程池隔离（舱壁模式）===");
        Service inventory = new Service("库存服务", 2);
        Service price = new Service("价格服务", 2);
        Service coupon = new Service("优惠服务", 2);

        for (int i = 0; i < 5; i++) {
            inventory.submit(() -> {
                try {
                    TimeUnit.SECONDS.sleep(2); // 模拟慢依赖
                } catch (InterruptedException ignored) {
                }
            });
        }
        System.out.println("库存服务线程池被打满(active=" + inventory.active() + ")，但价格/优惠服务不受影响");
        System.out.println("价格服务 active=" + price.active() + "，优惠服务 active=" + coupon.active());
        System.out.println("=> 隔离避免雪崩；Sentinel 也支持线程数/信号量隔离");
    }
}
